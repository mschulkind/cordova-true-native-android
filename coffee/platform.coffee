TN.isAndroid = ->
  device? && device.platform.lastIndexOf('Android', 0) == 0

TN.isIphone = ->
  DeviceInfo? && DeviceInfo.platform.lastIndexOf('iPhone', 0) == 0

TN.isSimulator = ->
  # TODO(mschulkind): Fix on android.
  DeviceInfo.platform == 'iPhone Simulator'

TN.platformCond = (options) ->
  return options?.iphone?() if TN.isIphone()
  return options?.android?() if TN.isAndroid()
  null

TN.screenSize =
  width: 320
  height: 480
