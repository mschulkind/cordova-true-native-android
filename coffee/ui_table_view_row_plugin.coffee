TN.UI.TableViewRow = class TableViewRow extends TN.UI.View
  PLUGIN_NAME: 'tableviewrow'

  constructor: (options) ->
    super

    @selectable = options?.selectable ? true

    TN.platformCond(
      iphone: =>
        # Flip the selected property on then off to momentarily show the
        # selection.
        @addEventListener('click', =>
          if @selectable
            @setProperty('selected', true)
            _(=>
              @setProperty('selected', false)
            ).delay(100)
        )
    )

  setProperties: (properties, onDone) ->
    if properties?.selectable?
      @selectable = properties.selectable
      properties = _(properties).clone()
      delete properties.selectable

    super(properties, onDone)
