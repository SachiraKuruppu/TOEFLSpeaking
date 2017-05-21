# TOEFL Speaking

## Introduction
This is an android application designed to practice the TOEFL speaking task 1 and 2 while on the go.
It displays questions randomly from a list and provide a timers for preparation time (15s) and speaking time (45s).

## Adding New Questions
Questions available in the application are added as a string array resource within /app/src/main/res/values/strings.xml
A copy of the question list is available in question_list.txt for reference. Before adding new questions please check
question_list.txt to see if the same question or similar question is available. If not, add the question to 
/app/src/main/res/values/strings.xml as the last item (only these questions will be added to the application) and update
the question_list.txt by adding the question as the last question of the file.