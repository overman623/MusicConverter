# $Id: Application.mk 212 2015-05-15 10:22:36Z oparviai $
#
# Build library bilaries for all supported architectures
#

#APP_ABI := all #armeabi-v7a armeabi
APP_ABI := armeabi-v7a
APP_PLATFORM := android-22
APP_OPTIM := release
#APP_STL := stlport_static
APP_STL := c++_static
APP_CPPFLAGS := -fexceptions # -D SOUNDTOUCH_DISABLE_X86_OPTIMIZATIONS