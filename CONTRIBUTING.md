### Contributing

(For the purposes of this section, any non empty file that is submitted to this repository is considered 'code')

Scaling Feast openly welcomes contributors! However, you should make yourself aware of the following. Keep in mind that the following contributing guidelines can change at any time, without warning.

- If you wish to contribute, please follow the same code style already present in the repository. In particular, follow good documentation practices using JavaDoc, use egyptian style braces and use encapsulation with proper getters and setters.
    - By good documentation practices, I mean you sould add JavaDoc to any of your own classes or methods that are not overriding other methods from other classes (like event listeners). If you notice code in the repository that doesn't follow these conventions, you should open an issue with the 'documentation' label.
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
    - Any pull requests that don't follow these conventions will have these changes requested, and will not be accepted until these changes are made.
    - If you notice any code in the repository that doesn't follow these conventions, you should open an issue with the 'housekeeping' label.
- All code in any pull request you open in the repository immediately is subject to Scaling Feast's MIT license, which you should familiarize yourself with BEFORE opening a pull request. That is, code you submit has no special rights granted to it. You are still the author of this code, and will be credited as such, you just don't choose what rights your code has when submitted. In addition, any code you submit to this repository can be used in any way by the author of the repository, even if the code, in its original form, is not used in the final product. Again, you still remain the author of the work you submit, and will be credited as such. In short, you are not the **owner** of the content you submit, but you are the **original author**.
