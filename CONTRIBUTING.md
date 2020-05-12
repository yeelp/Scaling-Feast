# Contributing

Scaling Feast openly welcomes contributors! However, you should make yourself aware of the following. Keep in mind that the following contributing guidelines can change at any time, without warning. Be sure to read the general guidelines, then read the section corresponding to the type of content you are contributing.

## General Guidelines/Info

- All content in any pull request you open in the repository immediately is subject to Scaling Feast's MIT license, which you should familiarize yourself with BEFORE opening a pull request. That is, content you submit has no special rights granted to it. You are still the author of this content, and will be credited as such, you just don't choose what rights your content has when submitted. In addition, any content you submit to this repository can be used in any way by the author of the repository, even if the content, in its original form, is not used in the final product. Again, you still remain the author of the work you submit, and will be credited as such. In short, you are not the **owner** of the content you submit, but you are the **original author**.

- Unless asked to do so, **NEVER** open a draft pull request. If lots of people contribute, this will create a lot of draft pull requests, which clutters the repository. Only open a pull request when you are sure your changes are complete.

### Contributing Code

- If you wish to contribute code (Java source code, JSON files, etc.), please follow the same code style already present in the repository. In particular, follow good documentation practices using JavaDoc, use egyptian style braces and use encapsulation with proper getters and setters, when applicable.
    - By good documentation practices, I mean you should add JavaDoc to any of your own classes or methods that are not overriding other methods from other classes (like event listeners). If you notice code in the repository that doesn't follow these conventions, you should open an issue with the 'documentation' label.
    - Egyptian style braces, along with other code formatting conventions in the repository look like the following:
    ```
       public static void method(Object a, Object b)
       {
           //Code inside is indented by four spaces. Use hard or soft tabs, doesn't matter.
           switch(a.someEnumMethod())
           {
               case ENUMA:
                   doSomething();
                   break;
               default:
                   break;
           }
           //If blocks with a single line of code should be written like so. Empty else blocks can be included if it increases readability. Try-Catch blocks should also follow this style.
           if(b.someBooleanMethod())
           {
               b.singleLineIfBlock();
           }
           else
           {
               doSomethingElse();
           }
    ```
    - If you're using Eclipse, you're in luck. You can download yeelpPreferences.epf from this repository and import these preferences into your Eclipse workspace. This *should* bring your formatting in line with this repository's code formatting. Problems with this preferences file should be highlighted in issues marked with the housekeeping label.
    - Any pull requests that don't follow these conventions will have these changes requested, and will not be accepted until these changes are made.
    - If you notice any code in the repository that doesn't follow these conventions, you should open an issue with the 'housekeeping' label.
- Make sure you have verified that the changes/features you're making actually work as intended. Open pull requests with too many issues, conflicts or crashes will be closed. You can reopen it when your branch is stable.

### Contributing Images
- If you're contributing images, make sure that the images you contribute are your own work. Pull requests for images that you contribute that came from a copywritten external source will be closed and locked.
- Images must not be offensive

### Contributing Localization
- When contributing localization for another language, the localization strings should be complete. That is, every key in [en_us.lang](https://github.com/yeelp/Scaling-Feast/blob/master/src/main/resources/assets/scalingfeast/lang/en_us.lang) should be translated.
- Do your best to make sure translations are correct.
- Do not just use Google Translate to translate localization strings. Google Translate serves as a direct translation - word for word - from one language to another. Such translations miss the semantic conventions that make translations so difficult in the first place.
- If there are missing localization keys or if you notice errors in current translations, file an issue with the localization tag.

### Contributing to the Wiki
- If errors or content is missing from the wiki, open an issue with the wiki label.

### Contributing Elsewhere
Other contributions should follow the typical GitHub Contribution Guidelines.
