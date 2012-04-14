window.navigator =
  geolocation: {}

class Listenable
  constructor: ->
    @listeners = {}

  addEventListener: (type, listener) ->
    @listeners[type] ||= []
    @listeners[type].push(listener)

  removeEventListener: (type, listener) ->
    callbackIndex = @eventListeners[eventName]?.indexOf?(eventCallback)
    @listeners[eventName].splice(callbackIndex, 1)

  dispatchEvent: (event) ->
    if @listeners[event.type]
      for callback in @listeners[event.type]
        callback(event.type)

makeListenable = (obj) ->
  listenable = new Listenable
  obj.addEventListener = (type, listener) ->
    listenable.addEventListener(type, listener)
  obj.removeEventListener = (type, listener) ->
    listenable.removeEventListener(type, listener)
  obj.dispatchEvent = (event) -> listenable.dispatchEvent(event)

makeListenable(document)
makeListenable(window)

class Event
  initEvent: (type) ->
    throw "type required" unless type?
    @type = type

document.createEvent = (type) ->
  throw "type must be 'Events'" unless type == 'Events'
  new Event


# HACKITY HACK HACK
document.createElement = ->
  setAttribute: ->
document.documentElement =
  appendChild: ->

window.XMLHttpRequest = class
  open: ->
  send: ->

window.shouldRotateToOrientation = ->
  ''

window.prompt = (text, value) ->
  nativeExec(value, text)
