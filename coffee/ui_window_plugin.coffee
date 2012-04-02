TN.UI.Window = class Window extends TN.UI.Component
  PLUGIN_NAME: 'window'

  constructor: (options) ->
    super options

    @title = options?.title || ""

    # Explicit view constructors are only supported for windows, not for any
    # subclasses like tab controller.
    if @constructor == Window
      @private.constructView = options?.constructView
    else
      throw "constructView option not supported" if options?.constructView

    @addEventListener('destroyView', =>
      delete @view
    )

  open: (options) ->
    modal = options?.modal ? false

    @registerSelfAndDescendants()
    
    TN.platformCond(
      android: =>
        @createView()
        @view.addEventOnceListener('resize', (e) =>
          @constructView(e.width, e.height)
        )
    )

    Cordova.exec(
      null, null, @pluginID, 'open',
      [
        window: this
        modal: modal
      ]
    )

  close: ->
    Cordova.exec(null, null, @pluginID, 'close', [windowID: @tnUIID])

  createView: ->
    @view = new TN.UI.View(backgroundColor: 'white')
    @view.registerSelfAndDescendants()
    @view

  constructView: (width, height) ->
    @view.width = width
    @view.height = height

    @private.constructView?(@view)
