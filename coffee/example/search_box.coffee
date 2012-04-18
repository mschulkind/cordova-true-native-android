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
    #textField = null

    headerCell.batchUpdates(->
      textFieldFrame = new TN.GridCell(
        growMode: 'horizontal'
        view: new TN.UI.View(
          borderWidth: 1
          borderColor: 'black'
          borderRadius: 15
          backgroundColor: 'white'
        )
      )
      headerCell.add(textFieldFrame)

      #textField = new TN.UI.TextField(
        #hint: hint
        #backgroundColor: "red"
      #)

      #textField.addEventListener('change', (e) -> onChange?(e))
      #textField.addEventListener('done', (e) -> onDone?(e))

      #textFieldFrame.add(new TN.GridCell(
        #growMode: 'horizontal'
        #inheritViewSizeMode: 'height'
        #view: textField
      #))
    )

    #textField.sizeToFit(->
      #headerCell.layout()
    #)
  )

  headerCell.view
