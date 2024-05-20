import React from 'react';
import styled from 'styled-components';

const ResultsContainer = styled.section`
  width: 100%;
  max-width: 800px;
  margin-top: 20px;
`;

const ResultsHeader = styled.h2`
  font-size: 1.5em;
  margin-bottom: 10px;
  color: #4a90e2;
`;

const ResultsList = styled.ul`
  list-style: none;
  padding: 0;
  border: 1px solid #ddd;
  border-radius: 5px;
  background: #fff;
`;

const ResultItem = styled.li`
  padding: 10px;
  border-bottom: 1px solid #eee;

  &:last-child {
    border-bottom: none;
  }
`;

const Results = ({ results }) => (
  <ResultsContainer>
    <ResultsHeader>Search Results</ResultsHeader>
    <ResultsList>
      {results.map((result, index) => (
        <ResultItem key={index}>{result}</ResultItem>
      ))}
    </ResultsList>
  </ResultsContainer>
);

export default Results;
