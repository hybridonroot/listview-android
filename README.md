## DocPicker Library for Android (AndroidX)

A Doc Picker library for Android for selecting single/multiple files of any type.


## Setup
Step 1: Add the dependency

## Usage
Step 1: Declare and Initialize DocPicker.

#### Java
```java
ArrayList<String> docs = new ArrayList<>();
docs.add(DocPicker.DocTypes.PDF);
docs.add(DocPicker.DocTypes.MS_POWERPOINT);
docs.add(DocPicker.DocTypes.MS_EXCEL);
docs.add(DocPicker.DocTypes.TEXT);


DocPickerConfig pickerConfig = new DocPickerConfig()
        .setAllowMultiSelection(false)
        .setShowConfirmationDialog(true)
        .setExtArgs(docs);

DocPicker.with(this)
        .setConfig(pickerConfig)
        .onResult()
        .subscribe(new Observer<ArrayList<Uri>>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(ArrayList<Uri> uris) {
                //uris: list of uri
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });
```

#### Kotlin
```kotlin
val docs = arrayListOf<String>(
    DocPicker.DocTypes.PDF,
    DocPicker.DocTypes.MS_WORD,
    DocPicker.DocTypes.MS_POWERPOINT,
    DocPicker.DocTypes.MS_EXCEL,
    DocPicker.DocTypes.TEXT)

val pickerConfig = DocPickerConfig()
    .setShowConfirmationDialog(true)
    .setAllowMultiSelection(false)
    .setExtArgs(docs)

DocPicker.with(this)
    .setConfig(pickerConfig)
    .onResult()
    .subscribe({
        println ( "here is the list: $it" )
    },{
        println ( "error: ${it.printStackTrace()}")
    })
```

## Explanation:

#### 1. DocPickerConfig:
It is use to set the configuration.
1. **.setAllowMultiSelection(booleanValue)**: tells whether to select single file or multiple file.
2. **.setShowConfirmationDialog(booleanValue)**: tells whether to show confirmation dialog on selecting the file(only work in single file selection).
3. **.setExtArgs(stringArrayValue)**: this will help in filtering the docs base on these speficied extentions(values in stringArray).

eg.
```java
//Pick single file with confirmation dialog and set extentions arguments
ArrayList<String> docs = new ArrayList<String>();
docs.add(DocPicker.DocTypes.PDF);
docs.add(DocPicker.DocTypes.MS_POWERPOINT);
docs.add(DocPicker.DocTypes.MS_EXCEL);
docs.add(DocPicker.DocTypes.TEXT);


DocPickerConfig pickerConfig = new DocPickerConfig()
        .setAllowMultiSelection(false)
        .setShowConfirmationDialog(true)
        .setExtArgs(docs);
```


### URI:
We will be returning the list of Uri after selecting the files. That's why it is better to know about Uri first.

A Uniform Resource Identifier (URI) is a compact sequence of characters that identifies an abstract or physical resource.

In Android, Content providers manage access to a structured set of data. They encapsulate the data, and provide mechanisms for defining data security. Content providers are the standard interface that connects data in one process with code running in another process.
You can get almost all information from uri.
#### URI usages:
1. Get file from uri:
```java
File file = new File(uri.getPath());
```

2. Get mime from uri:
```java
String mimeType = getContentResolver().getType(uri);
```

3. Used in Glide:
```java
Glide.with(context)
     .load(uri)
     .into(imageView);
```