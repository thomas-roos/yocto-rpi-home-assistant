# asterisk-sounds-de transcodes the prompt set to GSM at build time with sox, so
# a native sox is required. meta-openembedded's recipe does not extend to native,
# hence this.
BBCLASSEXTEND = "native nativesdk"

# Trim PACKAGECONFIG to just GSM. The default pulls in alsa/pulseaudio (and via
# other options ffmpeg, flac, vorbis), none of which are needed to convert raw
# PCM to .gsm - and all of which would otherwise have to be built native too.
# PACKAGECONFIG[gsm] does not exist upstream; sox autodetects libgsm, so the
# entry only needs to declare the dependency.
PACKAGECONFIG = "gsm"
PACKAGECONFIG[gsm] = ",,libgsm"
