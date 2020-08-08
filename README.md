### Ох уж этот Мюнхгаузен!

(интерактивная аудиоистория)

https://thebaronmunchausen.com

<table>
<tr>
<td style="text-align: center;"><img style="margin-right:20px" src="https://github.com/gram7gram/Munhauzen/blob/master/demo-2.jpg" alt="Logo" /></td>
<td style="text-align: center;"><img src="https://github.com/gram7gram/Munhauzen/blob/master/demo-1.jpg"></td>
</tr>
</table>

## Вступление

На даное время повсеместно распространены текстовые нелинейные новеллы, где читатель, принимая решения, развивает ход событий в ту или иную сторону.
Мы же решили делать такие аудио-истории для мобильных устройств. (ИОС и АНдроид)
Ето наше второе приложение, первое пока забросили, набив шишки-дышки и разобравшись шо к чему, подготовив и вложившись в новый более качественный проект.

## Аудио-книга-игра

«Ох уж этот Мюнхгаузен» в целом – оригинальный линейный рассказ писателя Распре, но обыграный нами по-своему. Мюнхгаузен вновь созывает гостей и готовится перерассказывать слушателям свои невероятные приключения, и вот, он рассказывает, как вновь попал в обычную передрягу но готов выбраться необычным способом, но «…Но… не могу ума приложить, внученька, я уже не в той молодости, как же я выбрался из той передряги? Не подскажешь?» - спрашивает он, потеряв нить рассказа. И тут вступает ход слушателя (игрока). Ему дается право выбора (в основном из 4 вариантов, по-разному), где он отыгрывает роль славной внученьки, подсказывая дедушке ход истории. Правильный вариант (оригинальный) продолжает оригинальную историю, в то время, как неправильный обыгрывается, возвращая слушателя к той же проблеме (но есть несколько фишек с дополнительными (неоригинальными правильными вариантами).

##

Build staging Android APK

`./gradlew :android-en:assembleStaging, :android-ru:assembleStaging`

Build release Android APK

`./gradlew :android-en:assembleRelease :android-ru:assembleRelease`

Generated APK are in directory

`android-en/build/output/generated/apk/staging/universal.apk`

`android-ru/build/output/generated/apk/staging/universal.apk`


## Notes

`keytool -genkey -v -keystore debug.keystore -alias androiddebugkey -keyalg RSA -keysize 2048 -validity 10000`

Install Android Studio RoboVM plugin (same version as in build.gradle)

http://robovm.mobidevelop.com/downloads/releases/idea/org.robovm.idea-2.3.7-plugin-dist.jar

Fix RoboVM facets

https://dkimitsa.github.io/2018/05/04/idea-fixing-android-gradle-facet3/
