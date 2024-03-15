# Group 21's Bank OOP Project
## Read this before you do anything to the files pls

## Folder structure
.
└── OOP_GRP21_Proj/
    ├── bin (.class files go here)/
    │   ├── Account.class
    │   └── ...
    ├── data (.csv files go here)/
    │   ├── Account.csv
    │   └── ...
    ├── doc (javadoc files go here)/
    │   ├── account docs package
    │   ├── bank docs package
    │   └── Account.html
    └── src (.java files go here)/
        ├── account (put classes that mainly interface through account here)/
        │   ├── Account.java
        │   ├── CreditCard.java
        │   ├── Customer.java
        │   └── Insurance.java
        └── bank (put bank package classes in here)/
            ├── Bank.java
            ├── Branch.java
            └── InsuranceCatalog.java
### Data Files
Use relative pathing in your code to navigate to them ("data\[file_name.csv]")
### Class Files
class files are stored in the src/[package name]/ areas respectively


## Use this command to generate javadocs for the two packages from your terminal
javadoc -d doc src\account\* src\bank\* 
(if there are more packages add them as additional arguments to the terminal input in the format src\[package name]\*)
