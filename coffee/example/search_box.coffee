App.createSearchBoxHeaderView = (options) ->
  hint = options?.hint
  onChange = options?.onChange
  onDone = options?.onDone

  headerCell = new TN.GridCell(
    padding: 10
    inheritViewSizeMode: 'width'
    layoutMode: 'vertical'
    view: new TN.UI.View(
      backgroundColor: 'gray'
    )
  )

  # Wait until the parent tableview resizes us before adding the textField.
  headerCell.view.addEventOnceListener('resize', ->
    textField = null

    headerCell.batchUpdates(->
      textField = new TN.UI.TextField(
        hint: hint
        backgroundColor: 'white'
        borderColor: 'black'
        borderWidth: 1
        text: 'jZ'
      )

      textField.addEventListener('change', (e) -> onChange?(e))
      textField.addEventListener('done', (e) -> onDone?(e))

      headerCell.add(new TN.GridCell(
        growMode: 'horizontal'
        inheritViewSizeMode: 'height'
        view: textField
      ))
    )

    headerCell.addEventOnceListener('layout', ->
      textField.sizeToFit(->
        textField.setProperty('text', '')
        headerCell.layout()
      )
    )
  )

  headerCell.view
