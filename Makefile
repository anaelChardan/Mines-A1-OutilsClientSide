#------------------------------
# Makefile de Felix_Java-v2.0
#
# Version : 2.0
# Auteur  : Matthias Brun
# 
#------------------------------



#---------------------------------
# Organisation générale du projet
#---------------------------------

# Organisation des sources de code, de test et de configuration.
SRC=src
TEST=test
CONFIG=config

# Organistion de la construction.
BUILD=build
DOC=doc



#--------------------------------------------------------
# Configuration des outils de compilation et d'exécution
#--------------------------------------------------------

# Encodage des fichiers sources.
ENCODING=utf-8

# Compilateur Java
JAVAC=javac

# JVM Java
JAVA=java



#-------------------------------------------
# Configuration des outils de documentation
#-------------------------------------------

# Génération de documentation
JAVADOC=javadoc



#----------------------------------
# Configuration des outils de test
#----------------------------------

# Classpath intégrant JUnit, EasyMock et Jemmy.
JUNITPATH=/usr/share/java/junit4.jar
CGLIBPATH=/usr/share/java/cglib-nodep.jar
OBJENESISPATH=/usr/share/java/objenesis.jar
MOCKPATH=/usr/share/java/easymock.jar
JEMMYPATH=/usr/share/java/jemmy2.jar


# Décommenter pour utiliser l'interface texte de JUnit.
TESTRUNNER=junit.textui.TestRunner

# Décommenter pour utiliser l'interface swing de JUnit.
#TESTRUNNER=junit.swingui.TestRunner
#JUNITGRAPHICPATH=/usr/share/java/junit.jar

# Décommenter pour utiliser l'interface AWT de JUnit.
#TESTRUNNER=junit.awtui.TestRunner
#JUNITGRAPHICPATH=/usr/share/java/junit.jar

TESTPATH=$(JUNITGRAPHICPATH):$(JUNITPATH):$(CGLIBPATH):$(OBJENESISPATH):$(MOCKPATH):$(JEMMYPATH)


#---------------------------------------------------
# Configuration des outils de vérification statique
#---------------------------------------------------

# Organisation de checkstyle
CHECKSTYLE=java -jar ~/Logiciels/Checkstyle/checkstyle-6.2/checkstyle-6.2-all.jar
CHECKSTYLEFILE=checkstyle.xml

# Organisation de findbugs
FINDBUGS=~/Logiciels/Findbugs/findbugs-2.0.2-rc1/bin/findbugs
FINDBUGSOPTIONS=-effort:max -maxRank 20 -low -textui -progress 


#-------------------------
# Configuration du projet
#-------------------------

# Sourcepath et classpath du projet.
SOURCEPATH=$(SRC):$(TEST)
CLASSPATH=$(CONFIG):$(BUILD):$(TESTPATH)



#------------------------
# Organisation du projet
#------------------------

# Nom de base des packages.
PACKAGEBASE=felix

# Programme principale et programme de test associé.
MAINPROGRAM=$(PACKAGEBASE).Felix
TESTPROGRAM=$(PACKAGEBASE).FelixTestSuite


#-------------
# Compilation
#-------------
 
# Fichiers à compiler.
JAVAFILES= \
	$(SRC)/$(PACKAGEBASE)/*.java \
	$(TEST)/$(PACKAGEBASE)/*.java \

# Compilation des sources du projet.
compile :
	mkdir $(BUILD) && \
	$(JAVAC) -classpath $(CLASSPATH) -sourcepath $(SOURCEPATH) -d $(BUILD) -encoding $(ENCODING) $(JAVAFILES)



#-----------
# Exécution 
#-----------

# Lancement du programme.
launch :
	$(JAVA) -classpath $(CLASSPATH) $(MAINPROGRAM)


#---------------
# Documentation
#---------------

# Génération de la documentation javadoc.
documentation :
	$(JAVADOC) -classpath $(CLASSPATH) -sourcepath $(SOURCEPATH) -d $(DOC) -encoding $(ENCODING) \
		-subpackages $(PACKAGEBASE)



#-----------
# Nettoyage
#-----------

# Nettoyage du projet (suppression du répertoire build).
clean :
	rm -rf $(BUILD)

# Nettoyage du projet (suppression du répertoire build et de la documentation).
mrproper : clean
	rm -rf $(DOC)

# Nettoyer, compiler.
new : clean compile



#---------------
# Test logiciel
#---------------

# Tests du programme Felix.
test_felix :
	$(JAVA) -classpath $(CLASSPATH) $(TESTRUNNER) $(TESTPROGRAM)



#-----------------
# Qualité du code
#-----------------

# Vérifications statiques avec checkfile
checkstyle_felix :
	$(CHECKSTYLE) -c $(CHECKSTYLEFILE) -r $(SRC) -r $(TEST)

# Vérification statique avec findbugs (les fichiers doivent être compilés !)
findbugs_felix : 
	$(FINDBUGS) $(FINDBUGSOPTIONS) -auxclasspath $(CLASSPATH) .

