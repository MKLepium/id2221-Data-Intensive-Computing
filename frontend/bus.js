// Initialize the map
const mymap = L.map('map').setView([64.122518, -21.869172], 10.5); // Set the initial center and zoom level

// Add a tile layer (you can choose different tile providers)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19, // Maximum zoom level
}).addTo(mymap);

fetchData();
// Implement set interval after creating clear all markers function
// setInterval(fetchData, 5000);

function fetchData() {
// Define the URL to fetch JSON data from
const url = 'http://88.99.215.78:8080/bus/getData';
    
// Initialize an empty object to store the JSON data
let buses = [];

// Use the fetch API to make the HTTP request
fetch(url, {headers: {'Content-Type':'applicaton/json'}, method: 'GET'})
.then(response => {
    // Check if the response status is OK (200)
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    // Parse the JSON response
    return response.json();
})
  .then(data => {
      // Store the JSON data in the "buses" object
      buses = data.data;
      // You can now work with the "buses" object here
      console.log(buses);

      // remove markers
      //TODO 

      Object.keys(buses).forEach(bus => {
          let currRoute = buses[bus][2];
          let currLat = buses[bus][3];
          let currLon = buses[bus][4];
          createNumberMarker(currRoute, currLat, currLon, 'route-'+currRoute).addTo(mymap);
        });
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
}
    
    //demo datas
// const buses = [
// {fer:'1-A', lat:64.1433266666667, lon:-21.9473833333333, route:'1'},{fer:'1-B', lat:64.0704133333333, lon:-21.9580666666667, route:'1'},
// {fer:'2-A', lat:64.1341383333333, lon:-21.87095, route:'2'},{fer:'3-A', lat:64.146405, lon:-21.9420666666667, route:'3'},
// {fer:'4-A', lat:64.11028, lon:-21.8957333333333, route:'4'}];

var markerDiv = document.createElement('div');
markerDiv.className = 'number-marker';

function createNumberMarker(number, lat, lon, css) {
    var markerDiv = document.createElement('div');
    markerDiv.className = 'number-marker ' + css;
    markerDiv.textContent = number;

    var customIcon = L.divIcon({
        className: 'custom-icon',
        html: markerDiv,
        iconSize: [30, 30],
    });

    return L.marker([lat, lon], { icon: customIcon });
}