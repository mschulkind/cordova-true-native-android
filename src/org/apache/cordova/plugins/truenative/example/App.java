package org.apache.cordova.plugins.truenative.example;

import android.os.Bundle;
import org.apache.cordova.plugins.truenative.TrueNativeActivity;

public class App extends TrueNativeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState,
        "spidermonkey.js",

        "underscore.js",
        "cordova-1.5.0.js",

        "environment.js",
        "countdown_callback.js",
        "date_time_util.js",
        "listenable.js",
        "platform.js",
        "ui_util.js",
        "util.js",

        "ui_component_plugin.js",
        "ui_view_plugin.js",
        "ui_action_sheet_plugin.js",
        "ui_button_plugin.js",
        "ui_date_picker_view_plugin.js",
        "ui_image_view_plugin.js",
        "ui_label_plugin.js",
        "ui_map_view_plugin.js",
        "ui_picker_view_plugin.js",
        "ui_scroll_view_plugin.js",
        "ui_spinner_plugin.js",
        "ui_table_view_plugin.js",
        "ui_table_view_row_plugin.js",
        "ui_text_field_plugin.js",
        "ui_window_plugin.js",
        "ui_mail_compose_window_plugin.js",
        "ui_navigation_controller_plugin.js",
        "ui_tab_controller_plugin.js",
        "ui_tab_plugin.js",

        "facebook_plugin.js",
        "file_plugin.js",
        "http_plugin.js",
        "location_autocomplete_plugin.js",
        "progress_hud_plugin.js",

        "grid_cell_control.js",

        "title_bar.js",
        "spinnerize.js",

        "example/environment.js",

        "example/location_selector_window.js",
        "example/search_box.js",

        "example/action_sheet_demo.js",
        "example/instagram_demo.js",
        "example/twitter_demo.js",
        "example/main.js");
  }
}
