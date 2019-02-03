const DEFAULT = "localhost:25570";
(document.getElementById(
    "text-input"
) as HTMLInputElement).defaultValue = DEFAULT;

function getValue() {
    return (
        (document.getElementById("text-input") as HTMLInputElement).nodeValue ||
        DEFAULT
    );
}

function updateButton() {
    let value = getValue();
    document.getElementById(
        "my-button"
    )!.nodeValue = `Add connection to '${value}'`;
}
document.getElementById("form")!.addEventListener("submit", submit);

/**
 * @param {{IPAddr: string}} form
 */
function submit() {
    browser.runtime.sendMessage({ addr: getValue() });
}
