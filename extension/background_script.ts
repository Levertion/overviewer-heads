const ports: (browser.runtime.Port | undefined)[] = [];

function addPort(port: browser.runtime.Port): number {
    const idx = ports.length;
    ports.push(port);
    return idx;
}
function remove_port(idx: number) {
    ports[idx] = undefined;
}
function get_port(idx: number): browser.runtime.Port {
    return ports[idx] as browser.runtime.Port;
}
function send_message(message: any) {
    ports.forEach(p => {
        if (p !== undefined) {
            p.postMessage(message);
        }
    });
}
const registered: browser.contentScripts.RegisteredContentScript[] = [];
interface AddressInfo {
    socket: WebSocket;
    info?: PlayerInformation;
}
interface PlayerInformation extends PlayerPosition {
    username: string;
}
interface PlayerPosition {
    x: number;
    y: number;
    z: number;
}
const connections: Map<string, AddressInfo> = new Map();

browser.runtime.onConnect.addListener(port => {
    console.log("Got port");
    const idx = addPort(port);
    port.onDisconnect.addListener(() => {
        remove_port(idx);
    });
});
browser.runtime.onMessage.addListener(newAddress);

function newAddress(value: any) {
    // Drop the promise on purpose
    newAddressInner(value);
    return;
}

async function newAddressInner(value: any) {
    const address: string = value.addr;
    if (connections.has(address)) {
        // Fail
    } else {
        const ws = new WebSocket(`ws://${address}`);
        const thisConnection: AddressInfo = { socket: ws };
        connections.set(address, thisConnection);
        ws.addEventListener("message", message => {
            const result: PlayerPosition & {
                username: string;
                reason: "update" | "connect" | "disconnect";
            } = JSON.parse(message.data);
            console.log(`Recieved: ${message.data}`);
            if (result.reason === "connect") {
                thisConnection.info = {
                    x: result.x,
                    y: result.y,
                    z: result.z,
                    username: result.username
                };
            }
            if (result.reason === "update") {
                thisConnection.info = {
                    username: result.username,
                    x: result.x,
                    y: result.y,
                    z: result.z
                };
            }
            if (result.reason === "disconnect") {
                if (thisConnection.info) {
                    send_message({
                        username: thisConnection.info.username
                    });
                    return;
                }
                thisConnection.info = undefined;
            }
            send_message(thisConnection.info);
        });
        ws.addEventListener("close", () => {
            connections.delete(address);
        });
    }
}
