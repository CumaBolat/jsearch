# JSearch

JSearch is a powerful and efficient Java-based search engine designed to index and search through documents seamlessly. It incorporates the Vector Space Model, TF-IDF weighting, and supports full-text searches. **All functionalities, including crawling, indexing, and searching, are implemented from scratch without the use of external libraries**.

## Screenshots

**CLI**

![image](https://i.ibb.co/c3mgLRM/Screenshot-from-2024-07-05-18-10-44.png)

**DESKTOP GUI**

![image2](https://i.ibb.co/rFRtQBc/Screenshot-from-2024-07-05-18-11-31.png)


## Features

- **Efficient Multithreading**: Utilizes multithreading to speed up indexing and searching. Extremely optimized, spends ~15 seconds to crawl and index 2gb of data.
- **Full Text Search**: Performs full-text search in the inverted index file. Extremely optimized, spends ~1 second to find in exactly which file contains 200 words long query inside 2gb of data.
- **Custom Algorithms**: Implements various custom algorithms for indexing and searching.
- **TF-IDF Weighting**: Enhances search accuracy with TF-IDF (Term Frequency-Inverse Document Frequency).
- **Keyword Boosting**: Adjusts search relevance based on term frequency and document frequency.
- **Cosine Similarity**: Ranks search results using cosine similarity.
- **Configurable Ignored Directories**: Allows users to specify directories to exclude from indexing.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
  - [Getting Started](#getting-started)
  - [Example](#example)
- [Code Overview](#code-overview)
  - [Main Components](#main-components)
  - [Key Methods](#key-methods)
  - [Customization](#customization)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgements](#acknowledgements)
- [TODO List](#todo-list)

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/jsearch.git
   cd jsearch
   ```

2. **Build the project**:
   Ensure you have Maven installed. Then run:
   ```bash
   mvn clean install
   cd frontend
   npm install
   ```

## Usage

### Getting Started

1. **Run the Application**:
   ```bash
   mvn clean compile exec:java
   ```

2. **Enter the Search Directory**:
   When prompted, enter the path to the directory you want to index. You can press `a` to index the entire system (Note: this can be time-consuming based on the directory size).

3. **Configure Ignored Directories**:
   By default, certain directories (e.g., `node_modules`, `.git`) are ignored. You can add additional directories or choose not to ignore any.

4. **Start Indexing**:
   The application will index the specified directory. This may take up to 10 minutes depending on the size.

5. **Search for Queries**:
   Once indexing is complete, you can enter your search queries. Both full-text search and vector space model search operations will executed. The results will be displayed based on their relevance.

### Example

```bash
# Start the application
$ mvn clean compile exec:java

# Follow the prompts:
# Enter the search directory path (Press a for the whole computer):
/home/user/documents

# Enter directories to ignore (Press n to ignore none):
node_modules .git

# Enter your search query (Press q to quit):
How do I traverse a Graph?

## Results will be listed here
```

## Code Overview

### Main Components

- **App.java**: The entry point of the application. Handles user inputs and orchestrates indexing and searching.
- **FileCrawler.java**: Crawls through directories and files to build the index using multithreading.
- **Searcher.java**: Manages the search logic and interfaces with different search models.
- **VectorSpaceModel.java**: Implements the Vector Space Model for search using TF-IDF, cosine similarity, and boolean logic.

### Key Methods

- **computeTfIdfWeights**: Calculates the TF-IDF weights for terms.
- **calculateCosineSimilarity**: Computes cosine similarity between query and documents.
- **getKeywordBoosts**: Calculates boosts for keywords based on their occurrence.
- **performBooleanSearch**: Processes boolean search queries and returns relevant documents.

### Customization

#### Ignored Directories

You can modify the default ignored directories in `App.java`:

```java
private static void addDefaultIgnoredDirectories() {
  ignoredDirectories.addAll(Arrays.asList("node_modules", "target", ".git", "rbenv", ".idea", ".rspec", ".steam", ".gradle", "words.txt", "cache", "logs", "build", "dist", "bin", "obj", "out", "vendor", "tmp", "temp", "examples", "samples"));
}
```

#### Boost Logic

The keyword boosting logic is implemented in `VectorSpaceModel.java`:

```java
private Map<String, Double> getKeywordBoosts(List<String> query, Map<String, Map<Integer, List<List<Integer>>>> queryIndexes) {
  Map<String, Double> keywordBoosts = new HashMap<>();
  Map<String, Integer> keywordFoundInDifferentFilesCount = new HashMap<>();
  Map<String, Integer> totalOccurrenceOfWord = new HashMap<>();

  for (String term : query) {
    keywordFoundInDifferentFilesCount.put(term, queryIndexes.get(term).size());
  }

  for (String term : query) {
    if (!queryIndexes.containsKey(term)) continue;
    
    int totalOccurrence = 0;
    for (Map.Entry<Integer, List<List<Integer>>> entry : queryIndexes.get(term).entrySet()) {
      totalOccurrence += entry.getValue().size();
    }
    totalOccurrenceOfWord.put(term, totalOccurrence);
  }

  // Implement your boosting logic here using keywordFoundInDifferentFilesCount and totalOccurrenceOfWord

  return keywordBoosts;
}
```

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Special thanks to my advisor [Prof. Dr. Bekir Taner Dincer](https://www.linkedin.com/in/bekir-taner-dincer/) for all his guidance.

## TODO List

- [ ] Implement advanced keyword boosting based on additional heuristics.
- [x] Optimize the multithreading implementation for faster indexing.
- [ ] Enhance the user interface for better user experience.
- [ ] Include more detailed logging and error handling.
