
# BatteryAlarm Android App

## Guidelines
### Issue Reporting
* Report the issue and choose bug report or feature request. The template includes all the information we need to track down the issue.
* This repository is only for issues within the BatteryAlarm Android app code. Issues in other components should be reported in their own repositories.
* Search the existing issues first, it's likely that your issue was already reported. If your issue appears to be a bug, and hasn't been reported, open a new issue.

## Contributing to Source Code
### Branching model
* All contributions bug fix or feature PRs target the master branch
* Feature releases will always be based on master
* Bug fix releases will always be based on their respective feature-release-bug-fix-branches
* Bug fixes relevant for the most recent and released feature (e.g. 2.0.0) or bugfix (e.g. 2.0.1) release will be backported to the respective bugfix branch (e.g. 2.0.x or 2.1.x)
* Hot fixes not relevant for an upcoming feature release but the latest release can target the bug fix branch directly

### Android Studio formatter setup
* Standard Android Studio
* Line length 120 characters (Settings->Editor->Code Style->Right margin(columns): 120)
* Auto optimize imports (Settings->Editor->Auto Import->Optimize imports on the fly)

### Contribution process
* Contribute your code in the branch 'master'. It will give us a better chance to test your code before merging it with stable code.
* For your first contribution start a pull request on master.

### Fork and download android repository
* Commit your changes locally: git commit -a
* Push your changes to your GitHub repo: git push
* Browse to https://github.com/YOURGITHUBNAME/android/pulls and issue pull request
* Enter description and send pull request.

