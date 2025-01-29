# Branching Strategy for Our Project

## Overview
This branching strategy is designed to streamline our development process, allowing us to work in subgroups without affecting each other's work. The main focus is to maintain stability in the codebase while enabling flexibility for feature development and task-based work during sprints.

### Main Branches
We will have two primary branches in our repository:

1. **`main` Branch**
   - This branch contains **production-ready code**.
   - Only stable, thoroughly tested, and reviewed code should be pushed to `main`.
   - We will commit to this branch from releae branch during a daily scrum after we have looked at everything together and prepared for sprint review

2. **`release` Branch**
   - The `release` branch acts as a **staging area for upcoming releases**.
   - This branch will handle the integration of features/phases and hotfixes before they are ready to be merged into the `main` branch.
   - All new development work starts from this branch.
   

### Supporting Branches

We will have supporting branches to manage feature development, tasks, and hotfixes/integration:

1. **Phase Branches**
   - The `Phase` branches acts as a **staging area for features**.
   - Naming Convention: `phase/phase0`, `phase/phase1`, `phase/phase2`, etc.
   - Each phase represents a major milestone or feature set in our sprints.
   - Think of these branches as the individual phases we pulled for the sprint. As the phases are completed, we will merge them into release branch.

3. **Sub-goal (Task) Branches**
   - Naming Convention: `phase-name/task-name`
   - Created from: `phase/phase-name`
   - Merged into: `phase/phase-name`
   - Sub-goal branches are used to work on individual tasks within a feature/phase.
   - These branches help us work in parallel without impacting each other's progress.
   - After the task is complete, merge it into the appropriate feature/phase branch and delete the sub-goal branch to keep the repository clean.

4. **Hotfix/debug/integrate Branches**
   - Naming Convention: `phase-numbr/hotfix-description`, `phase-number/debug/debug-goal or description`, `release/integration0,1,2` 
   - Created from: `release` or `main` or  `phase/phase-name`
   - Merged into: `release` and/or `main` and/or `phase/phase-name`
   - These branches are used to quickly address critical/debug issues in the production code.
   - Once the issue is resolved, it is merged back into `release` and/or `main` and/or `phase/phase-name` to ensure that all branches are updated with the fix.
   - these branches are made for anything that could break the current working code

## Workflow Summary

1. **Developing Features**
   - one person will make the `phase/phase-number` branch
   - Start by creating a sub-goal branch from the phase branch.
   - Work on the task. After the task is complete, merge the sub-goal branch into its phase branch and delete it.
   - Once all tasks for a feature/phase are complete, merge the phase branch into its feature branch.
   - Do not delete the phase branch. Once a phase is complete merge it into the release branch.

2. **Preparing for Release**
   - When a release is complete, the `release` branch is stabilized and tested.
   - Once the phase passes all checks and we talk about it, it is merged into the `main` branch to deploy the product-ready code.

3. **Handling Hotfixes/breakable debug workflows**
   - Create a hotfix branch from `main` or `release` or `phase/phase-name` if a critical issue arises.
   - Once fixed, merge the hotfix back into both `main` and/or `release` and/or `phase/phase-name` to ensure consistency.

## Example Branch Names

- **Main branch**: `main`
- **Release branch**: `release`
- **Phase branch**: `phase/phase0`
- **sub-goal task branch**: `phase0/dice`
- **Hotfix branch**: `hotfix/casino-disappears`

This structure will help us maintain a clean and organized codebase while allowing us to work in subgroups efficiently and independently.


## Benefits of This Branching Strategy (don't have to read)

- **Isolation of Work**: Allows subgroups to work on separate branches without affecting other teams.
- **Build Stability**: Ensures that `main` contains only production-ready code, preventing build failures.
- **Organized Workflow**: Clear structure for handling phases, features, sub-goals, and hotfixes.
- **Sprint Alignment**: Tasks and sub-goals align with our sprint planning, allowing for efficient progress tracking.

## Best Practices


- **Keep Branches Clean**: Delete sub-goal branches after merging to keep the repository organized.
- **Communicate Changes**: Regularly update the team about changes in branch statuses and progress on tasks.
- **Pull Before Merging**: before merging your task branch to the parent branch update your parent origin branch by pulling and merge any changes into your task to make sure your code works with the current parent branch state. 

