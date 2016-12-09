## CS273: Data and Knowledge Base
### GitHub data - Jack Alexander and Harshitha Chidananda

### Operating the code:

The project depends on Java, (specifically Java 8) and Maven. Please install these if you do not have them already.

#### Training and running a topic model

There are 3 steps to downloading data from a GitHub repository and trainging a model from it. Each requires an ID be passed for uniquely identifying the model. The provided data, which already has a model built for it, has ID 1. To start a prompt that will accept queries and print relevant users, run

```
./run.sh prompt 1
```

To start a server at http://localhost:4567/ run:


```
./run.sh serve 1
```

If you wish to train your own model with a new id `<id>`, run the following:


```
./run.sh get <id>   # Pull issue and user data from the GitHub repository
./run.sh run <id>   # Builds and saves a topic model
./run.sh score <id> # Assigns a topic distribution to each GitHub user based on the previously generated topic model.
```

You can then run the new model as described above with:

```
./run.sh prompt <id>
```

Or

```
./run.sh serve <id>
```

`<id>` can be any alphanumeric string.

Each of the training steps will take significant time (up to several minutes) to run, so if the process isn't finishing, give it time.

### Project Proposal: 

**Problem Statement**
The aim of the project is to use existing data in large well-developed GitHub repositories to build a knowledge base that can assist in the continued development of the project and in more quickly resolving issues posted to the repository. 

Problem: 
Currently, issues and pull requests for large projects are manually curated. 

Proposed solution: 
By providing a queryable, largely unsupervised means of tracking input from developers and users, we can significantly improve the efficiency of project curators.

___

**Where do you get the data?**
The data is obtained from a GitHub repository or repositories using the GitHub API, likely through the Java API package (link below). It will include descriptions and comments on pull requests and issues. We may also use project code, drawn either from the changelist of a pull request or from markdown in the issue or pull request. The first project we intend to use as a testing ground is the Linux GitHub. Later development may include the RocksDB library and any repositories in the list of “trending repositories.”
GitHub: https://developer.github.com/v3/
Java GitHub API: https://github.com/eclipse/egit-github/tree/master/org.eclipse.egit.github.core
Linux GitHub: https://github.com/torvalds/linux
Trending Repositories: https://github.com/trending

___



**What are you going to deliver?**
We will build a system that delivers the following services for a particular repository:
Recommend people who could work on the issues in the repository based on their previous work
Combining similar issues which are worded differently into one issue
Use Natural Language Processing to get meaningful data from text in the repository, including the status of the repository’s features
Draw relationship between issues,  pull requests and users
If possible (stretch goal), perform analysis on the changes to the codebase and analyze relationships between these changes and their descriptions (for example, commit messages)

___


**Milestones:**

Due Date | Milestones to be achieved
--- | --- 
Oct 18 | **Proposal:** Define problem statement, proposed solution, find data and make a project. proposal. 
Oct 25 | **Dataset:** Finalizing on Dataset, preprocessing and normalizing the  obtained data.
Nov 1 | **Algorithms:** Analyze various algorithms and finalize on what algorithms would work best for the obtained data.
Nov 8 | **Developing relationships:** Developing relationships between various entities in the data. 
Nov 15 | **Results:** Obtain results after algorithms and Natural Language Processing is applied on the dataset
Nov 22 | **Optimization:** Optimizing algorithms and performing experiments to check if there is any room for improvement.
Nov 29 | **Performance study:** Do Performance study and put everything together. Check if the project can be extended to other GitHub repositories 
Dec 6 | **Documentation and Report:** Document every step undertaken, decisions made, algorithms used, what went well, what didn’t go well, results, scope for future improvement. 
