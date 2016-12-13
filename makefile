JFLAGS = -sourcepath ./src
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
    src/com/kylegoodale/keval/client/ServerConnection.java \
    src/com/kylegoodale/keval/client/KevalClient.java \
    src/com/kylegoodale/keval/server/CommandHandler.java \
    src/com/kylegoodale/keval/server/Command.java \
    src/com/kylegoodale/keval/server/DataType.java \
    src/com/kylegoodale/keval/server/ListType.java \
    src/com/kylegoodale/keval/server/SetType.java \
    src/com/kylegoodale/keval/server/StringType.java \
    src/com/kylegoodale/keval/server/Database.java \
    src/com/kylegoodale/keval/server/ClientConnection.java \
    src/com/kylegoodale/keval/server/KevalServer.java \
    src/com/kylegoodale/keval/Main.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
