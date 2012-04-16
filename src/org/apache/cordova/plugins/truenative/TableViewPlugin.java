package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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

  private class TableViewAdapter extends ArrayAdapter<JSONObject> {
    private TableViewPlugin mPlugin;
    private ListView mTableView;

    public TableViewAdapter(
        Context context, ListView tableView, TableViewPlugin plugin) {
      super(context, 0, new ArrayList<JSONObject>());
      mPlugin = plugin;
      mTableView = tableView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      if (view == null) {
        JSONObject viewOptions;
        try {
          viewOptions = new JSONObject(
              mPlugin.writeJavascriptForComponent(mTableView, "createRow()"));
          view = (View)mPlugin.createComponent(viewOptions);

          mPlugin.writeJavascriptForComponent(
              mTableView, 
              "constructRow(-1, "+position+", "
              +mPlugin.getComponentID(view)+")");
        } catch(JSONException e) {
          e.printStackTrace();
          fail();
        }
      }

      mPlugin.writeJavascriptForComponent(
          mTableView, 
          "reuseRow(-1, "+position+", "+mPlugin.getComponentID(view)+")");

      return view;
    }
  }

  @Override
  protected Object newComponentInstance() {
    return new TableViewSubclass(getDroidGap(), this);
  }

  @Override
  protected ComponentData newComponentDataInstance() {
    return new TableViewData();
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    super.setupComponent(component, options);

    ListView tableView = (ListView)component;
    tableView.setAdapter(new TableViewAdapter(getDroidGap(), tableView, this));
    tableView.setCacheColorHint(Util.parseColor("clear"));

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
    } else {
      super.setComponentProperty(component, key, value);
    }
  }
}
