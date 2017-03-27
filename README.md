
# Contribute

## Setup

Some dependencies used in this project are not available on public maven repository yet and require
to build them locally.

First clone all repositories : 

	$ git clone https://github.com/mgsx-dev/gdx-pd.git
	$ git clone https://github.com/mgsx-dev/gdx-kit.git
	$ git clone https://github.com/mgsx-dev/rainstick.git

then install locally gdx-pd and gdx-kit (see update section)

configure android SDK locally by create a file local.properties in project root folder. For instance :

```
sdk.dir=/home/user/android-sdk-linux
```

finally import as gradle project rainstick worktree in Eclipse (or your favorite IDE)

## Update

First fetch and pull all 3 repositories (gdx-pd, gd-kit and rainstick)

Then build and install both gdx-pd and gdx-kit with gradle : 
```
cd ~/git/gdx-pd
./gradlew publishToMavenLocal
cd ~/git/gdx-kit
./gradlew publishToMavenLocal

```

Then in eclipse update rainstick project (right click / refresh gradle project)

## Run desktop

In rainstick-desktop project, locate DesktopLauncher.java and run it as Java application. First time it may
crash because of wrong working directory : edit the launcher and configure working directory, set rainstick-android project asset folder.

