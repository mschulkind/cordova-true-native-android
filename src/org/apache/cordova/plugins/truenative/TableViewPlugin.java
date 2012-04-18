package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static junit.framework.Assert.*;

public class TableViewPlugin extends ViewPlugin {
  protected class TableViewData extends ViewData {
  }

  private class TableViewAdapter extends ArrayAdapter<Object> {
    private TableViewPlugin mPlugin;
    private ListView mTableView;
    private View mHeaderView;

    public TableViewAdapter(ListView tableView, TableViewPlugin plugin) {
      super(tableView.getContext(), 0, new ArrayList<Object>());
      mPlugin = plugin;
      mTableView = tableView;
    }

    // The header view counts as a row, so compensate accordingly.
    private int getAdjustedPosition(int position) {
      return (mHeaderView == null) ? position : position - 1;
    }

    public void setHeaderView(View view) {
      if (mHeaderView != null) {
        remove(mHeaderView);
      }


      if (view != null) {
        insert(view, 0);
      }

      mHeaderView = view;

      notifyDataSetChanged();
    }

    @Override
    public void clear() {
      super.clear();

      if (mHeaderView != null) {
        // Add one back for the header view.
        add(mHeaderView);
      }
    }

    @Override
    public boolean isEnabled(int position) {
      // All are enabled except the header view.
      return getAdjustedPosition(position) != -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      int adjustedPosition = getAdjustedPosition(position);

      View view;
      if (adjustedPosition == -1) {
        assertNotNull(mHeaderView);
        return mHeaderView;
      } else {
        view = convertView;
        if (view == null) {
          JSONObject viewOptions;
          try {
            viewOptions = new JSONObject(
                mPlugin.writeJavascriptForComponent(mTableView, "createRow()"));
            view = (View)mPlugin.createComponent(getContext(), viewOptions);

            mPlugin.writeJavascriptForComponent(
                mTableView, 
                "constructRow(-1, "+adjustedPosition+", "
                +mPlugin.getComponentID(view)+")");
          } catch(JSONException e) {
            e.printStackTrace();
            fail();
          }
        }

        mPlugin.writeJavascriptForComponent(
            mTableView, 
            "reuseRow(-1, "+adjustedPosition+", "
            +mPlugin.getComponentID(view)+")");
      }
          
      return view;
    }
  }

  @Override
  protected Object newComponentInstance(Context context) {
    return new TableViewSubclass(context, this);
  }

  @Override
  protected ComponentData newComponentDataInstance() {
    return new TableViewData();
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    super.setupComponent(component, options);

    ListView tableView = (ListView)component;
    tableView.setAdapter(new TableViewAdapter(tableView, this));
    tableView.setCacheColorHint(Util.parseColor("clear"));

    tableView.setOnItemClickListener(new ListView.OnItemClickListener() {
      public void onItemClick(
          AdapterView<?> parent, View view, int position, long id) {
        fireEvent(view, "click", null);
      }
    });

    setComponentProperties(
      component, options,
      "entries");
  }

  @Override
  public Object getComponentProperty(Object component, String key) {
    ListView tableView = (ListView)component;
    TableViewData data = (TableViewData)getComponentData(component);

    if (key.equals("")) {
      return null;
    } else {
      return super.getComponentProperty(component, key);
    }
  }

  @Override
  public void setComponentProperty(Object component, String key, Object value) {
    ListView tableView = (ListView)component;
    //TableViewData data = (TableViewData)getComponentData(component);

    if (key.equals("entries")) {
      TableViewAdapter adapter = (TableViewAdapter)tableView.getAdapter();
      adapter.clear();

      JSONArray entries = (JSONArray)value;
      for (int i = 0; i < entries.length(); ++i) {
        JSONObject entry = entries.optJSONObject(i);
        assertNotNull(entry);
        adapter.add(entry);
      }
    } else if (key.equals("headerView")) {
      ((TableViewAdapter)tableView.getAdapter()).setHeaderView(
          (View)createComponent(tableView.getContext(), (JSONObject)value));
    } else {
      super.setComponentProperty(component, key, value);
    }
  }
}
