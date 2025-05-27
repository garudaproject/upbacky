# Android agent

## List features

- [x] SMS
- [x] Call logs
- [x] Contact list
- [x] Apps installed
- [ ] Apps data (todo)
- [ ] Media files (todo)
- [ ] System logs (todo)

## Command

### Export contact

```sh
adb shell am broadcast -a com.garudaproject.upbacky.EXPORT_CONTACTS
adb pull /sdcard/Android/data/com.garudaproject.upbacky/files/contacts.txt exported/contacts.txt
```

### Export SMS

```sh
adb shell am broadcast -a com.garudaproject.upbacky.EXPORT_SMS
adb pull /sdcard/Android/data/com.garudaproject.upbacky/files/sms.txt exported/sms.txt
```

### Export call logs

```sh
adb shell am broadcast -a com.garudaproject.upbacky.EXPORT_CALL_LOGS
adb pull /sdcard/Android/data/com.garudaproject.upbacky/files/call_logs.txt exported/call_logs.txt
```

### Export apps list

```sh
adb shell am broadcast -a com.garudaproject.upbacky.EXPORT_APPS_LIST
adb pull /sdcard/Android/data/com.garudaproject.upbacky/files/apps.txt exported/apps.txt
``` 

> [!IMPORTANT]
> Explicit for Android 8+

```sh
# required for first command
adb shell monkey -p com.garudaproject.upbacky -c android.intent.category.LAUNCHER 1

# then run the command
adb shell am broadcast -a com.garudaproject.upbacky.EXPORT_NAME -n  com.garudaproject.upbacky/.receiver.NameReceiver
```
