import React from 'react';
import styled from 'styled-components';

const SideBarContainer = styled.div`
  background-color: #fff;
  border-right: 1px solid #ddd;
  padding: 20px;
  width: 250px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
`;

const SideBarHeader = styled.h2`
  font-size: 1.2em;
  margin-bottom: 20px;
  color: #4a90e2;
`;

const SideBarList = styled.ol`
  list-style: decimal inside;
  padding-left: 20px;
`;

const SideBarItem = styled.li`
  margin-bottom: 10px;
`;

const SideBar = () => (
  <SideBarContainer>
    <SideBarHeader>How To Use JSearch:</SideBarHeader>
    <SideBarList>
      <SideBarItem>Enter Directory</SideBarItem>
      <SideBarItem>Wait for indexing to complete</SideBarItem>
      <SideBarItem>Search</SideBarItem>
      <SideBarItem>Wait for results</SideBarItem>
    </SideBarList>
  </SideBarContainer>
);

export default SideBar;
