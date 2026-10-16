# Android-Device-Setup — Blocker-Dokumentation (Galaxy A56 + Huawei Y600)

**Datum:** 2026-10-16  
**Ort:** Raspberry Pi 400 (Linux 6.18.50+rpt-rpi-v8, aarch64)  
**Arbeitsverzeichnis:** /home/mert/Desktop/flylab/flylab

## Zusammenfassung

Weder das Samsung Galaxy A56 noch das Huawei Y600 werden von `adb devices` oder `lsusb` sichtbar. Die Geräte sind **nicht erreichbar**. Das ist ein harter Blocker für das direkte Installieren/Testen der FlyLab-Debug-APK auf den physischen Geräten. Es wird trotzdem an allem anderen weitergearbeitet (Repos klonen, Commit, Push).

---

## (1) adb-Server neu gestartet

```
adb kill-server && adb start-server
* daemon not running; starting now at tcp:5037
* daemon started successfully
adb devices
List of devices attached
```

Ergebnis: Keine Geräte gelistet (leer).

---

## (2) `lsusb -v` nach den Geräten

`lsusb` listet nur:

- Bus 001 Device 001: Linux Foundation 2.0 root hub
- Bus 001 Device 002: VIA Labs, Inc. Hub (2109:3431)
- Bus 001 Device 003: USB Receiver (367b:1919)
- Bus 001 Device 004: Raspberry Pi Internal Keyboard (04d9:0007)
- Bus 002 Device 001: Linux Foundation 3.0 root hub

Es tauchen **keine** Android-/Samsung-/Huawei-Vendor-IDs auf. Auch `lsusb -v` zeigt keine mobilen Geräte.

---

## (3) adb kill-server / start-server / devices

Siehe (1): adb-Server läuft, aber `adb devices` bleibt leer.

---

## (4) `dmesg | tail` nach Plug-Events

Der Kernel erkennt ein Einstecken am USB-Port `1-1-port2`, kann das Gerät aber **nicht enumerieren**:

```
usb 1-1-port2: Cannot enable. Maybe the USB cable is bad?
usb 1-1-port2: attempt power cycle
usb 1-1.2: new high-speed USB device number 116 using xhci_hcd
usb 1-1.2: Device not responding to setup address.
usb 1-1.2: device not accepting address 116, error -71
usb 1-1-port2: unable to enumerate USB device
```

Fehler `-71` = USB-Protokoll-Enfehler (Typischerweise Kabel, Port oder Geräteseitig). Der Port wird wiederholt geklappt ("power cycle"), kommt aber nie zur Adresszuweisung. Es handelt sich um ein **Hardware-/Kabel-/Port-Problem**, nicht um ein adb- oder Treiber-Problem.

---

## (5) scrcpy

- `scrcpy` ist **nicht installiert**.
- Installationsversuch via `sudo apt-get install scrcpy` scheitert, weil `sudo` ein Passwort verlangt (kein Passwort gesetzt / kein TTY). Der Raspberry Pi 400 läuft hier ohne sudo-Rechte für diesen Nutzer.
- `apt-cache policy scrcpy` liefert keine Ausgabe (Paketindex nicht erreichbar / nicht installiert).

Scrcpy kann daher nicht nachinstalliert werden und wäre ohnehin nutzlos, solange kein Gerät über USB erkannt wird.

---

## Ursache (wahrscheinlich)

Die dmesg-Meldungen deuten stark auf eines dieser Probleme hin:

1. **Defektes USB-Kabel** (keine Datenleitung, nur Strom oder gar keine Verbindung).
2. **Defekter USB-Port** am Raspberry Pi 400 oder am angeschlossenen Hub.
3. Das Android-Gerät ist nicht wirklich im USB-Debugging-Modus oder der Bildschirm ist gesperrt (adb benötigt entsperrtes Gerät + bestätigte RSA-Fingerabdruck-Dialog).

Da `lsusb` die Vendor-ID der Telefone überhaupt nicht sieht, liegt das Problem vor der adb-Ebene (USB-Hardware-Enumeration).

---

## Was nötig wäre, um den Blocker zu lösen (manuell am Gerät)

- Ein anderes USB-Kabel probieren (nach Möglichkeit eines, das Daten übertragen kann, nicht nur Laden).
- Einen anderen USB-Port am Pi 400 probieren (Direkt, nicht über den VIA-Hub).
- Am Telefon: USB-Debugging prüfen, Entwickleroptionen aktivieren, "USB-Debugging" einschalten, Telefon entsperren, beim ersten Verbinden den adb-RSA-Fingerabdruck am Telefon bestätigen.
- Optional: `/etc/udev/rules.d/` mit Android-Udev-Regeln bestücken (z. B. Samsung 0x04e8, Huawei 0x12d1), danach `udevadm control --reload-rules`.
- scrcpy installieren (erfordert sudo/apt) für Bildschirm-Mirroring und Fernsteuerung, sobald das Gerät erkannt wird.

---

## Konsequenz für FlyLab

- Die Debug-APK kann **nicht** auf die Geräte installiert werden, solange der Blocker besteht.
- FlyLab lässt sich dennoch entwickeln und bauen (sofern Android SDK mit platforms/build-tools verfügbar wird — aktuell ist unter `/usr/lib/android-sdk` nur `platform-tools` vorhanden, also auch kein APK-Build möglich ohne weitere SDK-Komponenten).
- Alle geklonten GitHub-Repos und Code-Änderungen werden trotzdem committed und gepusht (siehe unten).

---

## Status

**BLOCKER** — Geräte nicht erreichbar. Arbeit an allem anderen wird fortgesetzt.