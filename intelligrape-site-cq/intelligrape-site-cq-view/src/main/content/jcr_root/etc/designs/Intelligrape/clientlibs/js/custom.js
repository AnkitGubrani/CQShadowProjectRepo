// JavaScript Document for Grepfrut

$(document).ready(function() {
	alert("reached here  ab");
	// ddsmoothmenu.init({
		// mainmenuid: "smoothmenu", //Menu DIV id
		// //Horizontal or vertical menu: Set to "h" or "v"
		// //class added to menu's outer DIV
		// method: 'hover', // set to 'hover' (default) or 'toggle'
		// classname: 'ddsmoothmenu',
		// arrowswap: false, // enable rollover effect on menu arrow images?
		// //customtheme: ["#804000", "#482400"],
		// contentsource: "markup" //"markup" or ["container_id",
		// "path_to_menu_file"]
		// })

		$('.ddsmoothmenu ul li a').on('mouseout', function() {
			var $this = $(this);
			$this.next().css( {
				"display" : "none",
				"visibility" : "hidden"
			});

		})
		
		$('.ddsmoothmenu ul li a').on('mouseover', function() {
			var $this = $(this);
			$this.next().css( {
				"display" : "block",
				"visibility" : "visible"
			});

		})

	});

function getParameterByName(name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex
			.exec(location.search);
	return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g,
			" "));
}
//function showMenuSlider() {
//	var $this = $(this);
//	$this.mouseout(function() {
//		console.log($this);
//		$this.next().css( {
//			"display" : "none",
//			"visibility" : "hidden"
//		});
//	}).mouseover(function() {
//		console.log($this);
//		$this.next().css( {
//			"display" : "block",
//			"visibility" : "visible"
//		});
//	});
//}

$(document)
		.ready(
				function() {
					var pathname = window.location.pathname;
					$("#pathname").val(pathname);
					var sent = getParameterByName('sent');
					if ((sent) == "yes") {
						$('#sentMassege')
								.text(
										"Thanks you for contacting us. We'll get back to you soon");

						jQuery(window).unload(function() {
							window.location.href = window.location.origin;
						});
					}
				});

function contactUs() {
	var $contactForm = $("#contact_form");
	if ($contactForm.valid()) {
		jQuery.each(navigationCookie.getValueAndResetCookie(), function(index,
				value) {
			var navPath = jQuery("<input>").attr("type", "hidden").attr("name",
					"navigationPath[]").val(value);
			jQuery("#contact_form").append(jQuery(navPath));
		});

		$("#myModal").modal('toggle');
		jQuery
				.post("/mail.php", $contactForm.serialize(), function() {
					location.href = '/thanks.html';
					$contactForm.find("input[type=text], textarea").val('');
				})
				.error(
						function() {
							alert("There was an error processing your request. Please try again later. Sorry about that! ");
							$contactForm.find("input[type=text], textarea")
									.val('');
						})
	}
	return false

}

function subscribe() {
	var $subform = $("#subform");
	if ($subform.valid()) {
		jQuery
				.post(
						"/newsletter.php",
						$subform.serialize(),
						function(data) {
							alert("You have been subscribed to our newsletter. Thank you!")
						})
				.error(
						function() {
							alert("There was an error processing your request. Please try again later. Sorry about that! ")
						})
	}

	return false;
}

// Tabs - In such way needs to initiate each tab
$('#myTab a').click(function(e) {
	e.preventDefault();
	$(this).tab('show');
})

// Thumbnail hover effect for gallery
$('.folio-thumb').hover(function() {
	$(this).find(".zoom, .link").fadeTo("fast", 1);
}, function() {
	$(this).find(".zoom, .link").fadeTo("fast", 0.5);
});

// accordion
$(".accordion-group").live('click', function() {
	var $self = $(this);
	$body = $self.find('.accordion-body');
	if ($self.find('.accordion-heading').hasClass('in_head')) {
		$self.parent().find('.accordion-heading').removeClass('in_head');
	} else {
		$self.parent().find('.accordion-heading').removeClass('in_head');
		$self.find('.accordion-heading').addClass('in_head');
	}
});

// Create the dropdown nav for responsive
$("<select />").appendTo(".ddsmoothmenu ");
$("<option />", {
	"selected" : "selected",
	"value" : "",
	"text" : "Menu"
}).appendTo(".ddsmoothmenu select");
// Populate dropdown with nav items
$(".ddsmoothmenu  a").each(function() {
	var el = $(this);
	$("<option />", {
		"value" : el.attr("href"),
		"text" : el.text()
	}).appendTo(".ddsmoothmenu select");
});
$(".ddsmoothmenu select").change(function() {
	window.location = $(this).find("option:selected").val();
});

// Below scripts do not require modification
/* global jQuery */
/*
 * ! FitVids 1.0
 * 
 * Copyright 2011, Chris Coyier - http://css-tricks.com + Dave Rupert -
 * http://daverupert.com Credit to Thierry Koblentz -
 * http://www.alistapart.com/articles/creating-intrinsic-ratios-for-video/
 * Released under the WTFPL license - http://sam.zoy.org/wtfpl/
 * 
 * Date: Thu Sept 01 18:00:00 2011 -0500
 */

(function($) {

	$.fn.fitVids = function(options) {
		var settings = {
			customSelector : null
		}

		var div = document.createElement('div'), ref = document
				.getElementsByTagName('base')[0]
				|| document.getElementsByTagName('script')[0];

		div.className = 'fit-vids-style';
		div.innerHTML = '&shy;<style>         \
      .fluid-width-video-wrapper {        \
         width: 100%;                     \
         position: relative;              \
         padding: 0;                      \
      }                                   \
                                          \
      .fluid-width-video-wrapper iframe,  \
      .fluid-width-video-wrapper object,  \
      .fluid-width-video-wrapper embed {  \
         position: absolute;              \
         top: 0;                          \
         left: 0;                         \
         width: 100%;                     \
         height: 100%;                    \
      }                                   \
    </style>';

		ref.parentNode.insertBefore(div, ref);

		if (options) {
			$.extend(settings, options);
		}

		return this
				.each(function() {
					var selectors = [ "iframe[src*='player.vimeo.com']",
							"iframe[src*='www.youtube.com']",
							"iframe[src*='www.kickstarter.com']", "object",
							"embed" ];

					if (settings.customSelector) {
						selectors.push(settings.customSelector);
					}

					var $allVideos = $(this).find(selectors.join(','));

					$allVideos
							.each(function() {
								var $this = $(this);
								if (this.tagName.toLowerCase() == 'embed'
										&& $this.parent('object').length
										|| $this
												.parent('.fluid-width-video-wrapper').length) {
									return;
								}
								var height = this.tagName.toLowerCase() == 'object' ? $this
										.attr('height')
										: $this.height(), aspectRatio = height
										/ $this.width();
								if (!$this.attr('id')) {
									var videoID = 'fitvid' + Math.floor(Math
											.random() * 999999);
									$this.attr('id', videoID);
								}
								$this
										.wrap(
												'<div class="fluid-width-video-wrapper"></div>')
										.parent('.fluid-width-video-wrapper')
										.css('padding-top',
												(aspectRatio * 100) + "%");
								$this.removeAttr('height').removeAttr('width');
							});
				});

	}
})(jQuery);

// Basic FitVids Test
$(".container, .row").fitVids();
// Custom selector and No-Double-Wrapping Prevention Test
$(".container, .row").fitVids( {
	customSelector : "iframe[src^='http://socialcam.com']"
});

// video z-index
$("iframe").each(function() {
	var ifr_source = $(this).attr('src');
	var wmode = "wmode=transparent";
	if (ifr_source.indexOf('?') != -1) {
		var getQString = ifr_source.split('?');
		var oldString = getQString[1];
		var newString = getQString[0];
		$(this).attr('src', newString + '?' + wmode + '&' + oldString);
	} else
		$(this).attr('src', ifr_source + '?' + wmode);
});

/*
 * IE Image Resizing - by Ethan Marcotte -
 * http://unstoppablerobotninja.com/entry/fluid-images/
 */
var imgSizer = {
	Config : {
		imgCache : [],
		spacer : "../images/spacer.gif"
	},
	collate : function(aScope) {
		var isOldIE = (document.all && !window.opera && !window.XDomainRequest) ? 1
				: 0;
		if (isOldIE && document.getElementsByTagName) {
			var c = imgSizer;
			var imgCache = c.Config.imgCache;

			var images = (aScope && aScope.length) ? aScope : document
					.getElementsByTagName("img");
			for ( var i = 0; i < images.length; i++) {
				images[i].origWidth = images[i].offsetWidth;
				images[i].origHeight = images[i].offsetHeight;

				imgCache.push(images[i]);
				c.ieAlpha(images[i]);
				images[i].style.width = "100%";
			}

			if (imgCache.length) {
				c
						.resize(function() {
							for ( var i = 0; i < imgCache.length; i++) {
								var ratio = (imgCache[i].offsetWidth / imgCache[i].origWidth);
								imgCache[i].style.height = (imgCache[i].origHeight * ratio)
										+ "px";
							}
						});
			}
		}
	},
	ieAlpha : function(img) {
		var c = imgSizer;
		if (img.oldSrc) {
			img.src = img.oldSrc;
		}
		var src = img.src;
		img.style.width = img.offsetWidth + "px";
		img.style.height = img.offsetHeight + "px";
		img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
				+ src + "', sizingMethod='scale')"
		img.oldSrc = src;
		img.src = c.Config.spacer;
	}

	// Ghettomodified version of Simon Willison's addLoadEvent() --
	// http://simonwillison.net/2004/May/26/addLoadEvent/
	,
	resize : function(func) {
		var oldonresize = window.onresize;
		if (typeof window.onresize != 'function') {
			window.onresize = func;
		} else {
			window.onresize = function() {
				if (oldonresize) {
					oldonresize();
				}
				func();
			}
		}
	}
}