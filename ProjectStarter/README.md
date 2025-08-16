# My Personal Project
# Startup Simulator
### Build. Ship. Grow. Simulate the journey of your own tech startup.

**Startup Simulator** is a java application that allows users to simulate launching and managing a tech startup. Users can define new product features, assign development resources, track budget, and watch their user base grow or shrink over time. The applicaition is designed for students, aspiring entrepreneurs, or tech enthusiasts who want to better understand how product development and business decisions impact a startup's growth. It's also useful for practiving basic project management and resource alloction in a fun, low stakes environment.
*Why this project?* I'm personally interested in tech startups and product design, and I want to build a tool that reflects real-world decision making while remaining manageable in scope. This project also allows me to explore clean software design, amd simulation logic, which are all key skills for my career in software developement. 

## User Stories

- As a user, I want to be able to **add a feature to my startup**, including detailes such as feature name, developement cost, estimated completion time, and expected user growth impact.

- As a user, I want to be able to **view a list of all features in my startup**, including their status (planned, in development, completed), cost, and user impact 

- As a user, I want to be able to **mark a feature as completed**, so that it starts generating user growth and stops using development budget

- As a user, I want to be able to **remove a feature** from my startup if I change my mind about building it

- As a user, I want to be able to **view current stats** for my startup, including total budget remaining, number of completed features, and estimated user base size.  

## Phase 2 User Stories

- As a user, I want to be able to have the option to save the current state of my startup (budget, features, and user base) to a file so I can resume progress later 

- As a user, I want to have the option to load the saved state of my startup from a file so I can pick up from where I left off


## Instructions for End User

- you can generate the first required action related to the user story "adding multiple features to a startup" by clicking the "Add Feature" button at the top of the window and filling in the prompted name, cost, estimated time, and user impact.

- you can generate the second required action related to the user story by removing feature from list by clicking remove feature button

- you can locate my visual component by looking at the center panel of the GUI, which displays a scrolable list of all features with their details (name, cost, time, user impact, and status)

- you can save the state of my application by clicking the "Save Startup" butoon at the top of the window. 

- You can reload the state of my application by clicking the "Load Startup" button at the top of the window.

-  You can generate the third required action related to the user story "adding multiple features to a startup" by clicking the "Mark Feature Completed" button and entering the name of the feature to complete.

- you can see a graph of the user base in the middle on the screen after a feature has been marked as completed


## Phase 4: task 2 
=== EVENT LOG START ===
Sun Aug 03 18:43:08 PDT 2025
Added feature: test1 (Cost: $5.0). Remaining budget: $995.0

Sun Aug 03 18:43:12 PDT 2025
Added feature: test 2 (Cost: $9.0). Remaining budget: $986.0

Sun Aug 03 18:43:18 PDT 2025
User base increased by 9 to 9

Sun Aug 03 18:43:26 PDT 2025
Added feature: test 3 (Cost: $19.0). Remaining budget: $967.0

Sun Aug 03 18:43:27 PDT 2025
Removed feature: test 3 (Refunded $19.0)

=== EVENT LOG END ===


## phase 4: Task 3
If i had more time to refactor the design, I would implement the following improvements.
I would introduce a Storage interface as currently JsonReader and JsonWriter are concrete implementations. I would create a interfce with load() and save() methods, then imlement handlers. I would also create a EventLogger interface to limit the amount of times I call EventLog.getInstance() directly. Overall I would reduce duplicated code by creating methods or interfaces. These changes would make it easier to maintain my code, make for better isolation for testing, and clearer boundaries between components.

I would Also refactor the GUI logic to improve seperation of concerns.
Currently, much of the user interaction, state management, and UI rendering are all withing a single class. This makes it large and difficult to test. A better design would be to make another class that deals with user actions. Eg. clicking "Add Feature" button could trigger a method in a controller class, which would then communicate with both the Startup model and teh GraphPanel view. this would improve readibility, modularity, and testability of the application logic.
