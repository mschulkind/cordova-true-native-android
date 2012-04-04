package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class ActionSheetPlugin extends ComponentPlugin {
  @Override
  public PluginResult execute(
      String action, JSONArray args, String callbackId) {
    JSONObject options;
    try {
      if (action.equals("show")) { this.show(args.getJSONObject(0)); }
      else { return super.execute(action, args, callbackId); }
    } catch (JSONException e) {
      return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    }

    return new PluginResult(PluginResult.Status.OK, "");
  }

  private class ContextClickListener
      implements MenuItem.OnMenuItemClickListener {
    private String mTNUIID;

    public ContextClickListener(String tnUIID) {
      mTNUIID = tnUIID;
    }

    public boolean onMenuItemClick(MenuItem item) {
      JSONObject data = new JSONObject();
      try {
        data.put("index", item.getItemId());
      } catch(JSONException e) {
        e.printStackTrace();
        fail();
      }
      ComponentPlugin.fireEvent(mTNUIID, "actionSheetClick", data);

      // TODO(mschulkind): Destroy the action sheet on the JS side.

      return true;
    }
  }

  private class ContextCreateListener 
      implements View.OnCreateContextMenuListener {
    JSONObject mOptions;

    public ContextCreateListener(JSONObject options) {
      mOptions = options;
    }

    public void onCreateContextMenu(
        ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
      try {
        JSONObject actionSheet = mOptions.getJSONObject("actionSheet");
        menu.setHeaderTitle(actionSheet.getString("title"));

        // Add all of the buttons.
        JSONArray buttons = 
            actionSheet.getJSONArray("buttons");
        for (int i = 0; i < buttons.length(); ++i) {
          JSONObject button = buttons.getJSONObject(i);
          MenuItem menuItem = menu.add(
              ContextMenu.NONE, i, ContextMenu.NONE, button.getString("title"));
          menuItem.setOnMenuItemClickListener(
              new ContextClickListener(actionSheet.getString("tnUIID")));
        }
      } catch(JSONException e) {
        e.printStackTrace();
        fail();
      }
    }
  };

  public void show(final JSONObject options) {
    ctx.runOnUiThread(new Runnable() {
      public void run() {
        View currentWindowView = WindowPlugin.openWindow.view;
        currentWindowView.setOnCreateContextMenuListener(
            new ContextCreateListener(options));
        currentWindowView.showContextMenu();
        currentWindowView.setOnCreateContextMenuListener(null);
      }
    });
  }
}
