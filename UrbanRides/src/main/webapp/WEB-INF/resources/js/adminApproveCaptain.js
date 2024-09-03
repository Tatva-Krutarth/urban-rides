function toggleAccordion(event, collapseId) {
    event.stopPropagation();
    const collapseElement = document.getElementById(collapseId);
    const bsCollapse = new bootstrap.Collapse(collapseElement, {
        toggle: true
    });
}

$(document).ready(function () {
    getCapData();
});

function getCapData() {
    $.ajax({
        url: 'admin-captain-all-data',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            populateAllRequests(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the query data");
        }
    });
}

function populateAllRequests(data) {
    const container = document.getElementById('accordionExample');
    container.innerHTML = ''; // Clear any existing content

    if (data.length === 0) {
        container.innerHTML = '<div class="no-captains-message">There are no captains to approve.</div>';
        return;
    }

    data.forEach(captain => {
        let totalDocsToVerify = 0;
        if (!captain.adharApprovedApprove) totalDocsToVerify++;
        if (!captain.drivingLicenceApprove) totalDocsToVerify++;
        if (!captain.rccertificateApprove) totalDocsToVerify++;
        if (!captain.drivingLicenceExpiryDateApprove) totalDocsToVerify++;
        if (!captain.rcexpirationDateApprove) totalDocsToVerify++;
        if (!captain.numberPlateApprove) totalDocsToVerify++;

        const card = document.createElement('div');
        card.className = 'card';

        card.innerHTML = `
            <div class="card-header noti-container p-0" id="heading${captain.captainId}">
                <div class="noti-img-cont">
                    <img src="/UrbanRides/resources/images/captain2.png" id="noti-img">
                </div>
                <div class="my-trip-accor-details-cont mt-2 mb-3">
                    <div class="service-type-text">Name :- <span class="my-trip-serviceType">${captain.captainName}</span></div>
                    <div class="service-type-text">Mobile No. :- <span class="my-trip-serviceType">${captain.phone}</span></div>
                    <div class="service-type-text">Email :- <span class="my-trip-serviceType">${captain.email}</span></div>
                    <div class="service-type-text">Created Date :- <span class="my-trip-serviceType">${captain.createdDate}</span></div>
                    <div class="service-type-text">Status :- <span class="my-trip-serviceType">${captain.status}</span></div>
                </div>
                <button id="accordion-button-${captain.captainId}" class="accordion-button-cls" onclick="toggleAccordion(event, 'collapse${captain.captainId}')">
                    View Details
                </button>
            </div>

            <div id="collapse${captain.captainId}" class="collapse" aria-labelledby="heading${captain.captainId}" data-parent="#accordionExample">
                <div class="card-body p-0">
                    <div class="trip-details-bottom-cont">
                        <div class="left-part">
                            ${!captain.adharApprovedApprove ? `
                                <div>
                                    <span>Adhar Card :-</span>
                                    <div class="left-part-right-side">
                                        <button id="adhar-card-link-${captain.captainId}" class="cap-app-btn-theme">View</button>
                                        <button id="approve-adhar-${captain.captainId}" class="cap-app-btn-theme">Approve</button>
                                        <button id="unapprove-adhar-${captain.captainId}" class="cap-app-btn-theme">Unapprove</button>
                                    </div>
                                </div>
                            ` : ''}
                            ${!captain.drivingLicenceApprove ? `
                                <div>
                                    <span>Driving License :-</span>
                                    <div class="left-part-right-side">
                                        <button id="driving-licence-link-${captain.captainId}" class="cap-app-btn-theme">View</button>
                                        <button id="approve-licence-${captain.captainId}" class="cap-app-btn-theme">Approve</button>
                                        <button id="unapprove-licence-${captain.captainId}" class="cap-app-btn-theme">Unapprove</button>
                                    </div>
                                </div>
                            ` : ''}
                            ${!captain.rccertificateApprove ? `
                                <div>
                                    <span>Registration Certificate :-</span>
                                    <div class="left-part-right-side">
                                        <button id="rc-certificate-link-${captain.captainId}" class="cap-app-btn-theme">View</button>
                                        <button id="approve-rc-${captain.captainId}" class="cap-app-btn-theme">Approve</button>
                                        <button id="unapprove-rc-${captain.captainId}" class="cap-app-btn-theme">Unapprove</button>
                                    </div>
                                </div>
                            ` : ''}
                            ${!captain.drivingLicenceExpiryDateApprove ? `
                                <div>
                                    <span>License Expiration Date :-</span>
                                    <div class="left-part-right-side">
                                    <span id="licence-expiry-date-${captain.captainId}">${captain.drivingLicenceExpiryDate}</span>
                                    <button id="approve-licence-expiry-${captain.captainId}" class="cap-app-btn-theme">Approve</button>
                                    <button id="unapprove-licence-expiry-${captain.captainId}" class="cap-app-btn-theme">Unapprove</button>
                                    </div>
                                </div>
                            ` : ''}
                            ${!captain.rcexpirationDateApprove ? `
                                <div>
                                    <span>RC Expiration Date :-</span>
                                    <div class="left-part-right-side">
                                    <span id="rc-expiration-date-${captain.captainId}">${captain.rcexpirationDate}</span>
                                    <button id="approve-rc-expiry-${captain.captainId}" class="cap-app-btn-theme">Approve</button>
                                    <button id="unapprove-rc-expiry-${captain.captainId}" class="cap-app-btn-theme">Unapprove</button>
                                    </div>
                                </div>
                            ` : ''}
                            ${!captain.numberPlateApprove ? `
                                <div>
                                    <span>Vehicle Number Plate :-</span>
                                    <div class="left-part-right-side">
                                        <span id="number-plate-${captain.captainId}">${captain.vehicleNumber}</span>
                                        <button id="approve-number-plate-${captain.captainId}" class="cap-app-btn-theme">Approve</button>
                                        <button id="unapprove-number-plate-${captain.captainId}" class="cap-app-btn-theme">Unapprove</button>
                                    </div>
                                </div>
                            ` : ''}
                        </div>
                    </div>
                    <input type="hidden" id="approved-docs-${captain.captainId}" value="">
                    <input type="hidden" id="unapproved-docs-${captain.captainId}" value="">
                    <input type="hidden" id="captain-id-${captain.captainId}" value="${captain.captainId}">
                    <button id="captain-approve-btn-${captain.captainId}" class="mb-3 cap-app-btn-theme-save" onclick="saveApproval(${captain.captainId}, ${totalDocsToVerify})">
                        Save
                    </button>
                </div>
            </div>
        `;

        container.appendChild(card);

        // Set links and button actions
        if (!captain.adharApprovedApprove) {
            setDocumentLink(`adhar-card-link-${captain.captainId}`, captain.adharCard, `approve-adhar-${captain.captainId}`, `unapprove-adhar-${captain.captainId}`, "adharApprove", "adharUnapprove", captain.adharApprovedApprove);
        }
        if (!captain.drivingLicenceApprove) {
            setDocumentLink(`driving-licence-link-${captain.captainId}`, captain.drivingLicence, `approve-licence-${captain.captainId}`, `unapprove-licence-${captain.captainId}`, "drivingApprove", "drivingUnapprove", captain.drivingLicenceApprove);
        }
        if (!captain.rccertificateApprove) {
            setDocumentLink(`rc-certificate-link-${captain.captainId}`, captain.rccertificate, `approve-rc-${captain.captainId}`, `unapprove-rc-${captain.captainId}`, "rcApprove", "rcUnapprove", captain.rCCertificateApprove);
        }
        if (!captain.drivingLicenceExpiryDateApprove) {
            setExpirationDateApproval(`approve-licence-expiry-${captain.captainId}`, `unapprove-licence-expiry-${captain.captainId}`, "drivingLicenceExpiryApprove", "drivingLicenceExpiryUnapprove", captain.drivingLicenceExpiryDateApprove);
        }
        if (!captain.rcexpirationDateApprove) {
            setExpirationDateApproval(`approve-rc-expiry-${captain.captainId}`, `unapprove-rc-expiry-${captain.captainId}`, "rcExpiryApprove", "rcExpiryUnapprove", captain.rCExpirationDateApprove);
        }
        if (!captain.numberPlateApprove) {
            setVehicleNumberPlateApproval(`approve-number-plate-${captain.captainId}`, `unapprove-number-plate-${captain.captainId}`, "numberPlateApprove", "numberPlateUnapprove", captain.isNumberPlateApprove);
        }
    });
}

function setDocumentLink(viewButtonId, url, approveButtonId, unapproveButtonId, approveString, unapproveString, isApproved) {
    let viewButton = document.getElementById(viewButtonId);
    let approveButton = document.getElementById(approveButtonId);
    let unapproveButton = document.getElementById(unapproveButtonId);

    viewButton.onclick = function () {
        window.open(url, '_blank');
    };

    approveButton.style.display = 'none';
    unapproveButton.style.display = 'none';

    if (!isApproved) {
        approveButton.style.display = 'inline';
        unapproveButton.style.display = 'inline';
    }

    approveButton.onclick = function () {
        updateApprovalStatus(viewButtonId, approveString, true);
        approveButton.classList.add('clicked-approve');
        unapproveButton.classList.remove('clicked-unapprove');
    };

    unapproveButton.onclick = function () {
        updateApprovalStatus(viewButtonId, unapproveString, false);
        unapproveButton.classList.add('clicked-unapprove');
        approveButton.classList.remove('clicked-approve');
    };
}

function setExpirationDateApproval(approveButtonId, unapproveButtonId, approveString, unapproveString, isApproved) {
    let approveButton = document.getElementById(approveButtonId);
    let unapproveButton = document.getElementById(unapproveButtonId);

    approveButton.style.display = 'none';
    unapproveButton.style.display = 'none';

    if (!isApproved) {
        approveButton.style.display = 'inline';
        unapproveButton.style.display = 'inline';
    }

    approveButton.onclick = function () {
        updateApprovalStatus(approveButtonId, approveString, true);
        approveButton.classList.add('clicked-approve');
        unapproveButton.classList.remove('clicked-unapprove');
    };

    unapproveButton.onclick = function () {
        updateApprovalStatus(unapproveButtonId, unapproveString, false);
        unapproveButton.classList.add('clicked-unapprove');
        approveButton.classList.remove('clicked-approve');
    };
}

function setVehicleNumberPlateApproval(approveButtonId, unapproveButtonId, approveString, unapproveString, isApproved) {
    let approveButton = document.getElementById(approveButtonId);
    let unapproveButton = document.getElementById(unapproveButtonId);

    approveButton.style.display = 'none';
    unapproveButton.style.display = 'none';

    if (!isApproved) {
        approveButton.style.display = 'inline';
        unapproveButton.style.display = 'inline';
    }

    approveButton.onclick = function () {
        updateApprovalStatus(approveButtonId, approveString, true);
        approveButton.classList.add('clicked-approve');
        unapproveButton.classList.remove('clicked-unapprove');
    };

    unapproveButton.onclick = function () {
        updateApprovalStatus(unapproveButtonId, unapproveString, false);
        unapproveButton.classList.add('clicked-unapprove');
        approveButton.classList.remove('clicked-approve');
    };
}

function updateApprovalStatus(docType, statusString, approved) {
    let captainId = docType.split('-').pop();
    let approvedDocs = document.getElementById(`approved-docs-${captainId}`).value.split(',').filter(Boolean);
    let unapprovedDocs = document.getElementById(`unapproved-docs-${captainId}`).value.split(',').filter(Boolean);
    if (approved) {
        if (!approvedDocs.includes(statusString)) {
            approvedDocs.push(statusString);
        }
        unapprovedDocs = unapprovedDocs.filter(id => id !== statusString.replace('Approve', 'Unapprove'));
    } else {
        if (!unapprovedDocs.includes(statusString)) {
            unapprovedDocs.push(statusString);
        }
        approvedDocs = approvedDocs.filter(id => id !== statusString.replace('Unapprove', 'Approve'));
    }
    document.getElementById(`approved-docs-${captainId}`).value = approvedDocs.join(',');
    document.getElementById(`unapproved-docs-${captainId}`).value = unapprovedDocs.join(',');
}

function saveApproval(captainId, totalDocsToVerify) {
    let approvedDocs = document.getElementById(`approved-docs-${captainId}`).value.split(',').filter(Boolean);
    let unapprovedDocs = document.getElementById(`unapproved-docs-${captainId}`).value.split(',').filter(Boolean);
    let captainIdValue = document.getElementById(`captain-id-${captainId}`).value;
    const allDocs = new Set([...approvedDocs, ...unapprovedDocs]);
    const totalVerifiedDocs = approvedDocs.length + unapprovedDocs.length;
    if (totalVerifiedDocs !== totalDocsToVerify) {
        showErrorMsg(`Please ensure all ${totalDocsToVerify} documents are either approved or unapproved.`);
        return;
    }
    let approvalData = {
        captainId: captainIdValue,
        verifiedDocId: approvedDocs,
        unverifiedDocId: unapprovedDocs
    };
    $(".loader").css("display", "flex");

    $.ajax({
        url: 'admin-approve-captain-docs',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(approvalData),
        success: function (response) {
            $(".loader").hide();
            const card = document.getElementById(`collapse${captainId}`);
            if (card) {
                card.classList.remove('show'); // Collapse the details section
            }
            const cardContainer = document.getElementById('accordionExample');
            const cardToRemove = document.getElementById(`heading${captainId}`).parentElement;
            if (cardToRemove) {
                cardContainer.removeChild(cardToRemove);
            }
            showSuccesstMsg("Data saved successfully");
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".loader").hide();
            try {
                const errorResponse = JSON.parse(xhr.responseText);
                if (typeof errorResponse === 'object' && !Array.isArray(errorResponse)) {
                    const messages = Object.values(errorResponse).join(', ');
                    showErrorMsg(messages);
                } else {
                    showErrorMsg("An unexpected error occurred: " + xhr.responseText);
                }
            } catch (e) {
                if (typeof xhr.responseText === 'string') {
                    showErrorMsg(xhr.responseText);
                } else {
                    showErrorMsg("An unexpected error occurred.");
                }
            }
        }
    });

}
