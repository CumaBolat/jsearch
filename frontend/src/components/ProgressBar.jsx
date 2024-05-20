import React from 'react';
import styled from 'styled-components';

const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  max-width: 800px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background: #fff;
  margin-top: 20px;
`;

const LoadingText = styled.p`
  font-size: 1.2em;
  color: #4a90e2;
`;

const LoadingSection = () => (
  <LoadingContainer>
    <LoadingText>Loading...</LoadingText>
  </LoadingContainer>
);

export default LoadingSection;
