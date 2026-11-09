# Voice Chair (Fabric, Minecraft 26.1.2)

## Inhalt (Kreativmodus, Reiter "Funktionsbloecke")
- **Jurystuhl**: Rechtsklick = hinsetzen (Shift = aufstehen). Anfangs rot.
- **Buzzer-Knopf** (Item):
  - Rechtsklick in die Luft: alle Stuehle im Umkreis (48 Bloecke) drehen sich um (Lied zu Ende).
  - Shift + Rechtsklick in die Luft: alle Stuehle zurueck auf rot.
  - Rechtsklick auf einen Stuhl: nur dieser Stuhl dreht sich.
  Umgedrehte Stuehle haben einen weissen Rand.
- **Namensfliese**: Neben den Stuhl legen (max. 3 Bloecke Abstand). Der Name erscheint in
  Weiss ueber der Fliese, sobald der Stuhl umgedreht ist.
  Name aendern: Namensschild im Amboss benennen, dann Rechtsklick auf die Fliese.

## Einstellen
- Sitzhoehe: `SEAT_Y_OFFSET` in `SeatManager.java`
- Namenshoehe: `NAME_Y_OFFSET` in `NameTileBlock.java`
- Reichweite des Buzzers: `RADIUS` in `ChairNetwork.java`

## Starten
1. Auf https://fabricmc.net/develop/template ein Projekt fuer 26.1.2 erzeugen und
   `gradlew`, `gradlew.bat` und den Ordner `gradle/` in diesen Ordner kopieren.
2. In IntelliJ oeffnen, Run-Config "Minecraft Client" starten.
3. JAR bauen: `gradlew build` -> `build/libs/` (Datei ohne "-sources"), mit Fabric API in den Mods-Ordner.

## Fertige JAR ohne eigene Installation (GitHub)
1. Kostenloses Konto auf github.com, neues Repository anlegen.
2. Den Inhalt dieses Ordners hochladen (inkl. des versteckten Ordners `.github`).
3. Reiter "Actions" -> der Build startet automatisch.
4. Nach ca. 3-5 Minuten den Lauf oeffnen und unten bei "Artifacts" `voicechair2-mod` herunterladen.
5. ZIP entpacken, die `.jar` in den `mods`-Ordner legen (zusammen mit der Fabric API).
