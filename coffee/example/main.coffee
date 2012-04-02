window.onDeviceReady = ->
  new TN.UI.Window(
    constructView: (view) ->
      view.setProperty('backgroundColor', 'green')
  ).open()
