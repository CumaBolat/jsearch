import React, { useState } from 'react';
import { createRoot } from 'react-dom/client';

import styled from 'styled-components';
import SearchBar from './components/SearchBar.jsx';
import SideBar from './components/Sidebar.jsx';
import Results from './components/Results.jsx';
import LoadingSection from './components/ProgressBar.jsx';

const AppContainer = styled.div`
  display: flex;
  min-height: 100vh;
  background-color: #f5f5f5;
  color: #333;
`;

const MainContent = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
`;

const Header = styled.header`
  background-color: #4a90e2;
  color: #fff;
  padding: 20px;
  text-align: center;
  width: 100%;
  font-size: 2em;
  margin-bottom: 20px;
  border-radius: 10px;
`;

const Footer = styled.footer`
  width: 100%;
  text-align: center;
  padding: 10px;
  background-color: #f5f5f5;
  color: #333;
  font-size: 0.9em;
  border-top: 1px solid #ddd;
  margin-top: auto;
`;

const App = () => {
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState([]);

  const handleSearch = async (query) => {
    setLoading(true);
    // Simulate an API call
    setTimeout(() => {
      setResults(['Result 1', 'Result 2', 'Result 3']);
      setLoading(false);
    }, 2000);
  };

  return (
    <AppContainer>
      <SideBar />
      <MainContent>
        <Header>JSearch</Header>
        <SearchBar onSearch={handleSearch} />
        {loading ? <LoadingSection /> : <Results results={results} />}
        <Footer>&copy; 2024 Your Name or Organization</Footer>
      </MainContent>
    </AppContainer>
  );
};

export default App;


const root = createRoot(document.getElementById('root'));
root.render(<App />);
