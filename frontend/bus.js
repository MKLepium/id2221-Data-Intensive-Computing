// Initialize the map
const mymap = L.map('map').setView([64.122518, -21.869172], 10.5); // Set the initial center and zoom level

// Add a tile layer (you can choose different tile providers)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19, // Maximum zoom level
}).addTo(mymap);

fetchData();

setInterval(fetchData, 5000); //5 seconds

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

      console.log(buses.length); //to see how many datapoints we get

      clearMarkers();
      addMarkers(buses);
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

function addMarkers(buses) {
    Object.keys(buses).forEach(bus => {
        let currRoute = buses[bus][2];
        let currLat = buses[bus][3];
        let currLon = buses[bus][4];
        createNumberMarker(currRoute, currLat, currLon, 'route-'+currRoute).addTo(mymap);
      });
}

function clearMarkers() {
    mymap.eachLayer(function (layer) {
        if (layer instanceof L.Marker) {
            mymap.removeLayer(layer);
        }
    });
}

function processRoutes() {
    getRoutes()
        .then(data => {
            // Handle the data here or pass it to another function
            console.log('Data received:', data);
            const parsedData = parseCSV(data);
            const routeNumberInput = document.getElementById('routeNumber');
            const routeNumber = routeNumberInput.value;

            const specificData = getRouteInfo(parsedData,routeNumber);
            const routeLongName = specificData.route_long_name;
            const outputDiv = document.getElementById('output');
            outputDiv.innerHTML = `Route Name for Route Number ${routeNumber}: ${routeLongName}`;
        })
        .catch(error => {
            // Handle errors here
            console.error('Error:', error);
            const outputDiv = document.getElementById('output');
            outputDiv.innerHTML = `Route number invalid`;
        });
}

function processStops() {
    getStops()
        .then(data => {
            // Handle the data here or pass it to another function
            console.log('Data received:', data);
            const parsedData = parseCSV(data);
            const stopNumberInput = document.getElementById('stopNumber');
            const stopNumber = stopNumberInput.value;

            const specificData = getStopInfo(parsedData, stopNumber);
            const stopName = specificData.stop_name;
            const outputDiv = document.getElementById('output');
            outputDiv.innerHTML = `Stop Name for Stop Number ${stopNumber}: ${stopName}`;
        })
        .catch(error => {
            // Handle errors here
            console.error('Error:', error);
            const outputDiv = document.getElementById('output');
            outputDiv.innerHTML = `Stop number invalid`;
        });
}

function parseCSV(text) {
    const lines = text.split('\n');
    const columns = lines[0].trim().split(',');
    const data = [];

    for (let i = 1; i < lines.length; i++) {
        const values = lines[i].trim().split(',');

        // Check if values is not empty before processing
        if (values.length === columns.length) {
            const entry = {};

            for (let j = 0; j < columns.length; j++) {
                entry[columns[j].trim()] = values[j].trim();
            }

            data.push(entry);
        }
    }

    return data;
}

function getRouteInfo(data, routeShortName) {
    return data.find(entry => entry.route_short_name === routeShortName);
}

function getStopInfo(data, stopId) {
    return data.find(entry => entry.stop_id === stopId);
}

function getRoutes() {
    return fetch('http://127.0.0.1:5500/frontend/gtfs-data/routes.txt')
      .then(response => response.text())
      .then((data) => {
        console.log(data);
        return data;
      });
  }

function getStops() {
    return fetch('http://127.0.0.1:5500/frontend/gtfs-data/stops.txt')
    .then(response => response.text())
    .then((data) => {
      console.log(data);
      return data;
    });
}
