import React from 'react';
import { ClockLoader } from 'react-spinners';
import './LoadingSpinner.css';


const LoadingSpinner = () => {
  return (
    <div className="spinner">
      <ClockLoader color='white' />
    </div>
  );
};

export default LoadingSpinner;