// Initialize the map
const mymap = L.map('map').setView([64.122518, -21.869172], 10.5); // Set the initial center and zoom level

// Add a tile layer (you can choose different tile providers)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19, // Maximum zoom level
}).addTo(mymap);

//demo data
const buses = [
{fer:'1-A', lat:64.1433266666667, lon:-21.9473833333333, route:'1'},{fer:'1-B', lat:64.0704133333333, lon:-21.9580666666667, route:'1'},
{fer:'2-A', lat:64.1341383333333, lon:-21.87095, route:'2'},{fer:'3-A', lat:64.146405, lon:-21.9420666666667, route:'3'},
{fer:'4-A', lat:64.11028, lon:-21.8957333333333, route:'4'}];

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

buses.forEach(bus => {
    createNumberMarker(bus.route, bus.lat, bus.lon, 'route-'+bus.route).addTo(mymap);
})
