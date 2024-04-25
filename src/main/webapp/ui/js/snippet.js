
/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

jQuery(document).ready(() => {
	let url = SailPoint.CONTEXT_PATH + '/plugins/pluginPage.jsf?pn=motd';

	jQuery("#preferenceMenu").parent().find(".dropdown-menu")
        .append(
        		'<li role="presentation">\n' +
				'  <a href="' + url +'" role="menuitem" class="menuitem">\n' +
				'    Message of the Day Settings' +
				'  </a>\n' +
				'</li>'
        );
});