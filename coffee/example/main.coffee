window.onDeviceReady = ->
  new TN.UI.Window(
    constructView: (view) ->
      view.setProperty('backgroundColor', 'black')

      red = new TN.UI.View(
        top: 10
        left: 20
        width: 30
        height: 40
        backgroundColor: 'red'
      )
      view.add(red)

      _(->
        red.setProperty('left', 50)
        view.add(new TN.UI.View(
          top: 80
          left: 20
          width: 30
          height: 40
          backgroundColor: 'cyan'
        ))
      ).delay(1000)
  ).open()
