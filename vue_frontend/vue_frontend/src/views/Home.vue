<template>
    <div class="center-container">
      <!-- Voeg hier eventuele inhoud toe voor Historie -->
      <router-link to="/actuele-data" class="clickable-wrapper" style="width: 10%;">
        <div class="center-image-container">
          <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Circle-icons-water.svg/2048px-Circle-icons-water.svg.png" alt="Water icoon" class="center-image" />
        </div>
      </router-link>
    </div>
  </template>
  
  <style>
  .center-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 90vh; /* Volledige hoogte van het scherm */
  }
  
  .center-image-container {
    text-align: center;
  }
  
  .center-image {
    max-width: 100%; /* Zorg ervoor dat de afbeelding niet breder is dan de container */
    max-height: auto; /* Zorg ervoor dat de afbeelding niet hoger is dan de container */
  }
  
  .clickable-wrapper {
    cursor: pointer;
    text-decoration: none; /* Verwijder onderstreepte stijl voor router-link */
    transition: width 0.3s; /* Voeg een overgang toe voor een vloeiende animatie */
  }
  
  .clickable-wrapper:hover {
    width: 10%; /* Verander de breedte wanneer de gebruiker er overheen zweeft */
  }
  </style>
  