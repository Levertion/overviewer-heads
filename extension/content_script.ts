//@ts-ignore
const { L, overviewer } = window.wrappedJSObject;
const server = "https://minotar.net/helm/<playername>/128";
console.log("in content script");
function toCoords(user: PlayerPosition) {
    return overviewer.util.fromWorldToLatLng(user.x, user.y, user.z);
}

function makeIcon(username: string, markerSize: number) {
    return L.icon(
        //@ts-ignore
        cloneInto(
            {
                iconUrl: server.replace("<playername>", username),
                iconSize: [markerSize, markerSize]
            },
            window
        )
    );
}

const players: Map<string, any> = new Map();
const port = browser.runtime.connect();
port.onMessage.addListener(i => {
    console.log("Got message" + JSON.stringify(i));
    const user: PlayerInformation = i as PlayerInformation;
    if (user.x === undefined) {
        overviewer.map.removeLayer(players.get(user.username));
    } else {
        if (user.y < 0) user.y = 0;
        if (user.y > 256) user.y = 256;
        var markerSize = Math.round(0.0859375 * user.y + 10) * 2;
        if (!players.has(user.username)) {
            const marker = L.marker(toCoords(user))
                .setIcon(makeIcon(user.username, markerSize))
                .addTo(overviewer.map);

            players.set(user.username, marker);
        }
        players
            .get(user.username)!
            .setLatLng(toCoords(user))
            .setIcon(makeIcon(user.username, markerSize));
    }
});
