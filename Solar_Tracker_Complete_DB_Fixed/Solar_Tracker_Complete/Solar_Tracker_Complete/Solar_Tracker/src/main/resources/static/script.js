const BASE_URL = "";

async function api(path, options = {}) {
    const response = await fetch(`${BASE_URL}${path}`, {
        headers: { "Content-Type": "application/json", ...(options.headers || {}) },
        ...options
    });

    const text = await response.text();
    let data = null;
    try { data = text ? JSON.parse(text) : null; } catch { data = text; }

    if (!response.ok) {
        throw new Error(data?.message || data?.error || `Server Error: ${response.status}`);
    }
    return data;
}

function value(id) { return document.getElementById(id).value.trim(); }
function numberValue(id) { return Number(value(id)); }

async function addInstallation() {
    const location = value("installationLocation");
    const capacityKw = numberValue("capacityKw");
    if (!location || !capacityKw || capacityKw <= 0) return alert("Enter installation location and a capacity greater than 0 kW.");

    try {
        const data = await api("/installations", {
            method: "POST",
            body: JSON.stringify({
                installationName: location,
                location,
                capacityKw
            })
        });
        alert(`Installation saved. ID: ${data.installationId}`);
        document.getElementById("installationLocation").value = "";
        document.getElementById("capacityKw").value = "";
        loadDashboard();
        loadInstallationOptions();
    } catch (error) { alert(`Save failed: ${error.message}`); }
}

async function addHousehold() {
    const installationId = value("installationId");
    const householdName = value("householdName");
    const allocationRatio = value("allocationRatio") ? numberValue("allocationRatio") / 100 : 1;
    if (!installationId || !householdName) return alert("Enter installation ID and household name.");

    try {
        const data = await api(`/households?installationId=${encodeURIComponent(installationId)}`, {
            method: "POST",
            body: JSON.stringify({ householdName, allocationRatio })
        });
        alert(`Household saved. ID: ${data.householdId}`);
        document.getElementById("householdName").value = "";
        document.getElementById("allocationRatio").value = "";
        loadDashboard();
        loadHouseholdOptions();
    } catch (error) { alert(`Save failed: ${error.message}`); }
}

async function addGeneration() {
    const installationId = value("genInstallationId");
    const generationDate = value("genDate");
    const unitsGenerated = numberValue("unitsGenerated");
    if (!installationId || !generationDate || unitsGenerated < 0) return alert("Enter installation ID, date and valid generated units.");

    try {
        await api(`/generation?installationId=${encodeURIComponent(installationId)}`, {
            method: "POST",
            body: JSON.stringify({ generationDate, unitsGenerated })
        });
        alert("Generation saved successfully.");
        document.getElementById("unitsGenerated").value = "";
        loadDashboard();
    } catch (error) { alert(`Save failed: ${error.message}`); }
}

async function addConsumption() {
    const householdId = value("householdId");
    const consumptionDate = value("consDate");
    const unitsConsumed = numberValue("unitsConsumed");
    if (!householdId || !consumptionDate || unitsConsumed < 0) return alert("Enter household ID, date and valid consumed units.");

    try {
        const data = await api(`/consumption?householdId=${encodeURIComponent(householdId)}`, {
            method: "POST",
            body: JSON.stringify({ consumptionDate, unitsConsumed })
        });
        alert(`Consumption saved. Generation share: ${data.generationShare} units, Exported: ${data.unitsExported} units.`);
        document.getElementById("unitsConsumed").value = "";
        loadDashboard();
    } catch (error) { alert(`Save failed: ${error.message}`); }
}

async function getSummary() {
    const householdId = value("summaryHousehold");
    const year = value("year");
    const month = value("month");
    if (!householdId || !year || !month) return alert("Enter household ID, year and month.");

    try {
        const data = await api(`/consumption/summary/monthly?householdId=${encodeURIComponent(householdId)}&year=${year}&month=${month}`);
        document.getElementById("summaryResult").innerHTML = `
            <strong>Monthly Summary</strong>
            <div>Days logged: ${data.daysLogged}</div>
            <div>Total consumed: ${data.totalConsumedUnits} units</div>
            <div>Generation share: ${data.totalGenerationShareUnits} units</div>
            <div>Exported: ${data.totalExportedUnits} units</div>`;
    } catch (error) { alert(`Summary error: ${error.message}`); }
}

async function loadDashboard() {
    try {
        const [installations, households, generations, consumptions] = await Promise.all([
            api("/installations"), api("/households"), api("/generation"), api("/consumption")
        ]);
        document.getElementById("totalInstallations").textContent = installations.length;
        document.getElementById("totalHouseholds").textContent = households.length;
        document.getElementById("totalGeneration").textContent = generations.length;
        document.getElementById("totalConsumption").textContent = consumptions.length;
    } catch (error) { console.error("Dashboard load failed:", error); }
}

async function loadInstallationOptions() {
    try {
        const installations = await api("/installations");
        const selectIds = ["installationId", "genInstallationId"];
        selectIds.forEach(id => {
            const select = document.getElementById(id);
            if (!select || select.tagName !== "SELECT") return;
            select.innerHTML = '<option value="">Select installation</option>' +
                installations.map(i => `<option value="${i.installationId}">${i.installationId} - ${escapeHtml(i.location)} (${i.capacityKw} kW)</option>`).join("");
        });
    } catch (error) { console.error(error); }
}

async function loadHouseholdOptions() {
    try {
        const households = await api("/households");
        ["householdId", "summaryHousehold"].forEach(id => {
            const select = document.getElementById(id);
            if (!select || select.tagName !== "SELECT") return;
            select.innerHTML = '<option value="">Select household</option>' +
                households.map(h => `<option value="${h.householdId}">${h.householdId} - ${escapeHtml(h.householdName)}</option>`).join("");
        });
    } catch (error) { console.error(error); }
}

function escapeHtml(text) {
    return String(text).replace(/[&<>'"]/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;","'":"&#39;","\"":"&quot;"}[c]));
}

window.addEventListener("DOMContentLoaded", () => {
    const today = new Date().toISOString().slice(0, 10);
    document.getElementById("genDate").value = today;
    document.getElementById("consDate").value = today;
    document.getElementById("year").value = new Date().getFullYear();
    document.getElementById("month").value = new Date().getMonth() + 1;
    loadDashboard();
    loadInstallationOptions();
    loadHouseholdOptions();
});
