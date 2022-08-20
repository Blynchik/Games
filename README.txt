��� ��������� � ������� IDEA:

1.������� JavaFx �� ������ https://gluonhq.com/products/javafx/ � �����������
2.������� ����������� � �����������
3.������� � IDEA Games-master\src\sample\StartMe.java
4.�������� ���������� ������ #39 String JAVA_FX_LIB �� ���� � ����� lib � ������ � JavaFx.
  ��������, C:\User\Desktop\javafx-sdk-18.0.2\lib
5.� IDEA ������� File->Project Structure->Libraries � ������� lib �������� "-". ����������� - "Aplly"
6.��� �� �������� "+" �������� ����� ����������. ���������� ��������� � ����� lib ������ JavaFx. ����������� - "Aplly"
��������, C:\User\Desktop\javafx-sdk-18.0.2\lib
7.��������� StartMe.java (��� � ������ ����:Error: JavaFX runtime components are missing, and are required to run this application)
8.� IDEA ������� Run->Edit Configurations->Modify options->Add VM Options. � ������ VM options ��������:
--module-path "���� � ����� lib � JavaFx(������� ��������)" --add-modules ALL-MODULE-PATH
9.��� �� � ������ Working Directory ������ �� ���� �� ����� src.
��������, C:\User\Desktop\Games-master\src
10.��������� StartMe.java 


��� ��������� � ������� ��������� ������:

1.������� JavaFx �� ������ https://gluonhq.com/products/javafx/ � �����������
2.������� ����������� � �����������
3.������� � IDEA  ��� � ��������� ��������� Games-master\src\sample\StartMe.java
4.�������� ���������� ������ #39 String JAVA_FX_LIB �� ���� � ����� lib � ������ � JavaFx.
  ��������, C:\User\Desktop\javafx-sdk-18.0.2\lib
5.��������� ��������� ������ � ������ � �������� ����� ���������(Games-master/src)
  ���
  ������ � �������� ����� ���������(Games-master/src), ������ Shift, ������ ��� � ������� "������� ���� PowerShell �����"
6.������ �������(������� ������): javac --module-path "���� � ����� lib � JavaFx(������� ��������)" --add-modules ALL-MODULE-PATH -d out/production/Engine sample/StartMe.java
                                  java --module-path "���� � ����� lib � JavaFx(������� ��������)" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.StartMe
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

How to run with IDEA:

1.Download JavaFx from the link https://gluonhq.com/products/javafx/ and unpack
2.Download the repository and unpack
3.Open in IDEA Games-master\src\sample\StartMe.java
4.Change the contents of line #39 String JAVA_FX_LIB to the path to the lib pack in the JavaFx package.
  For example C:\User\Desktop\javafx-sdk-18.0.2\lib
5.In IDEA go to File->Project Structure->Libraries and delete lib by pressing "-" and apply.
6.In the same place by pressing "+" add a new library. The library is located in the lib pack in the JavaFx package and apply.
For example C:\User\Desktop\javafx-sdk-18.0.2\lib
7.Run StartMe.java (It's OK: Error: JavaFX runtime components are missing, and are required to run this application)
8.In IDEA, go to Run->Edit Configurations->Modify options->Add VM Options. In the VM options line, paste:
--module-path "Path to the lib pack in the JavaFx package (leave quotes)" --add-modules ALL-MODULE-PATH
9.In the same place check path to the root in the Working Directory line.
Sample: C:\User\Desktop\Games-master\src
10.Run StartMe.java

How to run using command prompt:

1.Download JavaFx from the link https://gluonhq.com/products/javafx/ and unpack
2.Download the repository and unpack
3.Open in IDEA or in a text editor Games-master\src\sample\StartMe.java
4.Change the contents of line #39 String JAVA_FX_LIB to the path to the lib pack in the JavaFx package.
  For example C:\User\Desktop\javafx-sdk-18.0.2\lib
5.Start the command prompt and go to the root of the program (Games-master/src)
  or
  Go to the root of the program (Games-master/src), hold down Shift, press RMB and select "Open PowerShell here"
6.Enter commands (long string): javac --module-path "Path to the lib in the JavaFx (leave quotes)" --add-modules ALL-MODULE-PATH -d out/production/Engine sample/StartMe.java
                                java --module-path "Path to the lib in the JavaFx (leave quotes)" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.StartMe

