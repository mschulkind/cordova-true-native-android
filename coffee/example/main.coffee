window.onDeviceReady = ->
  new TN.UI.Window(
    createView: (view) ->
      view.setProperty('backgroundColor', 'green')
  ).open()
