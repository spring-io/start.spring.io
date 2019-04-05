import '../styles/index.scss';

$.fn.radio = function () {
    $(this).each(function () {
        const options = $(this).find('.radio');
        const optionsLinks = options.find('a');
        const input = $(this).find('input');
        optionsLinks.click(function () {
            const radio = $(this).parent();
            options.removeClass('active');
            radio.addClass('active');
            input.val($(this).data('value'))
                .trigger('change');
            return false;
        });
    });
};

$.fn.dropdown = function() {
    const _this = $(this);
    const body = $('body');
    body.click(function() {
        _this.each(function () {
            $(this).removeClass('open');
        });
    });
    _this.each(function () {
        const dropdown = $(this);
        const toggle = $(this).find('.dropdown-toggle');
        toggle.click(function (e) {
            dropdown.addClass('open');
            e.stopImmediatePropagation();
        })
    });
}

$(function () {

    const nameInput = $("input[name='name']");
    const groupIdInput = $("input[name='groupId']");
    const artifactIdInput = $("input[name='artifactId']");
    const packageNameInput = $("input[name='packageName']");

    const generatePackageName = function () {
        let groupId = groupIdInput.val();
        let artifactId = artifactIdInput.val();
        let packageName = groupId.concat(".").concat(artifactId).replace(/-/g, '');
        packageNameInput.val(packageName);
    };

    /**
     * Parse hash bang parameters from a URL as key value object.
     * For repeated parameters the last parameter is effective.
     * If = syntax is not used the value is set to null.
     * #!x&y=3 -> { x:null, y:3 }
     * @param url URL to parse or null if window.location is used
     * @return Object of key -> value mappings.
     * @source https://gist.github.com/zaus/5201739
     */
    const hashbang = function (url, i, hash) {
        url = url || window.location.href;
        let pos = url.indexOf('#!');
        if (pos < 0) return [];
        let vars = [], hashes = url.slice(pos + 2).split('&');
        for (i = hashes.length; i--;) {
            hash = hashes[i].split('=');
            vars.push({name: hash[0], value: hash.length > 1 ? hash[1] : null});
        }
        return vars;
    };

    const applyParams = function() {
        var params = hashbang();
        $.each(params, function( index, param ) {
            var value = decodeURIComponent(param.value);
            let element;
            switch(param.name)  {
                case 'type':
                case 'language':
                case 'packaging':
                case 'javaVersion':
                    element = $("a[data-value='" + value + "']");
                    element.click();
                    break;
                case 'groupId':
                case 'artifactId':
                case 'name':
                case 'description':
                case 'packageName':
                    element = $("input[name='" + param.name + "']");
                    element.val(value);
                    break;
            }
            element.trigger('change');
        });
    };

    groupIdInput.on("change", function() {
        generatePackageName();
    });
    artifactIdInput.on('change', function () {
        nameInput.val($(this).val());
        $("#baseDir").attr('value', this.value);
        generatePackageName();
    });

    // -------------------------------

    // Dropdown Component

    $('.dropdown').dropdown();

    // -------------------------------

    // Radio Component

    $('.radios').radio();

    if (navigator.appVersion.indexOf('Mac') != -1) {
        $('#btn-generate span.shortcut').html(' &#8984; + &#9166;');
    }
    else {
        $('#btn-generate span.shortcut').html(' alt + &#9166;');
    }

    applyParams();
});
