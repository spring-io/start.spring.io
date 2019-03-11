import * as JsSearch from 'js-search';
import {Versions} from './versions';

let listToAdd = [];

let listAdded = [];

let selected = -1;

let dependencies;

const TEMPLATE_DEP = `<div class="item {{ className }}" data-id="{{ id }}" data-index="{{ index }}">
    <div class="title">{{ name }} <span class="cat">[{{ group }}]</span></div>
    <div class="desc">{{ desc }}</div>
    <div class="incompatible">Requires Spring Boot {{ versionRequirement }}.</div>
    <a data-id="{{ id }}" class="btn-ico"><i class="fas fa-times"></i></a>
    <input type="hidden" name="style" value="{{ id }}">
</div>`;

const TEMPLATE_DEP_TO_ADD = `<div class="item {{ className }}" data-id="{{ id }}" data-index="{{ index }}">
    <div class="title">{{ name }} <span class="cat">[{{ group }}]</span></div>
    <div class="desc">{{ desc }}</div>
    <div class="incompatible">Requires Spring Boot {{ versionRequirement }}.</div>
    <a data-id="{{ id }}" class="btn-ico"><i class="fas fa-plus"></i></a>
</div>`;

const TEMPLATE_LIST = `<li class="{{ className }}">
    <label>
        <input {{ disabled }} {{ checked }} type="checkbox" value="true" data-id="{{ id }}"> 
        <strong>{{ name }}</strong>: {{ desc }}
        <span>Requires Spring Boot {{ versionRequirement }}.</span>
    </label>
</li>`;

const search = new JsSearch.Search('name');
search.addIndex('name');
search.addIndex('id');
search.addIndex('description');
search.addIndex('group');

function addToDep(item) {
    $('#inputSearch').val('');
    listAdded.push(item);
    updateDepsToAdd();
    rendersDep();
    renderDepsToAdd();
    $('#inputSearch').focus();
}

function removeFromDep(item) {
    listAdded = listAdded.filter((it) => it.id !== item.id);
    updateDepsToAdd();
    rendersDep();
    renderDepsToAdd();
}

function rendersDep() {
    const list = $('#list-added');
    const colDep = $('#col-dep');
    if (listAdded.length === 0) {
        colDep.addClass('hide');
        list.html('');
    } else {
        const version = new Versions();
        const bootVersion = $('#input-boot-version').val();
        const html = listAdded.map((obj, index) => {
            let result = TEMPLATE_DEP
                .replace(new RegExp('{{ id }}', 'g'), obj.id)
                .replace(new RegExp('{{ index }}', 'g'), index)
                .replace(new RegExp('{{ name }}', 'g'), obj.name)
                .replace(new RegExp('{{ desc }}', 'g'), obj.description)
                .replace(new RegExp('{{ versionRequirement }}', 'g'), obj.versionRequirement)
                .replace(new RegExp('{{ group }}', 'g'), obj.group);
            if (obj.versionRange && !version.matchRange(obj.versionRange)(bootVersion)) {
                result = result.replace(new RegExp('{{ className }}', 'g'), 'invalid')
                    .replace(new RegExp('<input.*>', 'g'), '');
            }
            else {
                result = result.replace(new RegExp('{{ className }}', 'g'), '');
            }
            return result;
        });
        list.html(html);
        list.find('.item').click(function () {
            const id = $(this).find('a').data('id');
            const item = dependencies.find((item) => item.id === id);
            removeFromDep(item);
        });
        colDep.removeClass('hide');
    }
    updateBodyClass();
}

function renderDepsToAdd() {
    const list = $('#list-to-add');
    const noResult = $('#noresult-to-add');
    let searchStr = $('#inputSearch').val().trim();
    if (listToAdd.length === 0) {
        if (searchStr !== '') {
            noResult.addClass('show');
        } else {
            noResult.removeClass('show');
        }
        list.html('');
    } else {
        const red = listToAdd.length > 5 ? listToAdd.slice(0, 6) : listToAdd;
        const version = new Versions();
        const bootVersion = $('#input-boot-version').val();
        const html = red.map((obj, index) => {
            let className = '';
            if (obj.versionRange && !version.matchRange(obj.versionRange)(bootVersion)) {
                className = 'invalid';
            }
            return TEMPLATE_DEP_TO_ADD
                .replace(new RegExp('{{ id }}', 'g'), obj.id)
                .replace(new RegExp('{{ index }}', 'g'), index)
                .replace(new RegExp('{{ name }}', 'g'), obj.name)
                .replace(new RegExp('{{ desc }}', 'g'), obj.description)
                .replace(new RegExp('{{ versionRequirement }}', 'g'), obj.versionRequirement)
                .replace(new RegExp('{{ className }}', 'g'), className)
                .replace(new RegExp('{{ group }}', 'g'), obj.group);
        });
        noResult.removeClass('show');
        list.html(html);
        applySelected();

        list.find('.item').mouseenter(function () {
            selected = $(this).data('index');
            applySelected();
        });
        list.find('.item').click(function () {
            const obj = $(this);
            const id = obj.find('a').data('id');
            if (obj.hasClass('invalid')) {
                return false;
            }
            const item = dependencies.find((item) => item.id === id);
            addToDep(item);
        });
    }
    updateBodyClass();
}

function updateDepsToAdd() {
    let searchStr = $('#inputSearch').val().trim();
    listToAdd = searchStr === '' ? [] : search.search(searchStr);
    listToAdd = listToAdd
        .filter((item) => listAdded.filter((i) => i.id === item.id).length === 0)
        .sort((a, b) => b.weight - a.weight);
}

function applySelected() {
    let items = $('#list-to-add .item');
    items.removeClass('active');
    if (selected > -1 && selected < items.length) {
        const element = $(items.get(selected));
        if (!isScrolledIntoView(element)) {
            $('body,html').scrollTop(element.offset().top);
        }
        element.addClass('active');
    }
}

function isScrolledIntoView(elem) {
    var docViewTop = $(window).scrollTop();
    var docViewBottom = docViewTop + $(window).height();
    var elemTop = $(elem).offset().top;
    var elemBottom = elemTop + $(elem).height() + 80;
    return ((elemBottom <= docViewBottom) && (elemTop >= docViewTop));
}

function isWindowScroll() {
    const wh = $(window).height();
    const bh = $('body').height();
    return bh > wh;
}

function updateBodyClass() {
    const body = $('body');
    if (isWindowScroll()) {
        body.addClass('fixed');
    } else {
        body.removeClass('fixed');
    }
}

function modalHeight() {
    const bodyModal = $('#modal-dependencies .modal-body');
    const wh = $(window).height();
    bodyModal.height(Math.min(wh - 250, 1000));
}

function populateDependencies(deps) {
    const bootVersion = $('#input-boot-version').val();
    $('#dependencies-list-title strong').html('Spring Boot ' + bootVersion);
    const version = new Versions();
    const grouped = {};
    var i;
    for (i = 0; i < deps.length; i++) {
        const item = deps[i];
        if (!grouped[item.group]) {
            grouped[item.group] = [];
        }
        grouped[item.group].push(item);
    }
    const keys = Object.keys(grouped);
    let template = '';
    for (i = 0; i < keys.length; i++) {
        const key = keys[i];
        const group = grouped[key];
        let list = '<ul>';
        template += `<div class="group"><h3><span>${key}</span></h3>`;
        list += group.map((obj, index) => {
            let className = '';
            let checked = '';
            let disabled = '';
            if (obj.versionRange && !version.matchRange(obj.versionRange)(bootVersion)) {
                className = 'invalid';
                disabled = 'disabled=""';
            }
            if (listAdded.find((item) => item.id === obj.id)) {
                checked = 'checked="checked"';
            }
            return TEMPLATE_LIST
                .replace(new RegExp('{{ checked }}', 'g'), checked)
                .replace(new RegExp('{{ id }}', 'g'), obj.id)
                .replace(new RegExp('{{ index }}', 'g'), index)
                .replace(new RegExp('{{ name }}', 'g'), obj.name)
                .replace(new RegExp('{{ desc }}', 'g'), obj.description)
                .replace(new RegExp('{{ versionRequirement }}', 'g'), obj.versionRequirement)
                .replace(new RegExp('{{ className }}', 'g'), className)
                .replace(new RegExp('{{ disabled }}', 'g'), disabled)
                .replace(new RegExp('{{ group }}', 'g'), obj.group);
        }).join('');
        list += '</ul></div>';
        template += list;
    }
    $('#dependencies-list').html(template);

    /*
    $('#dependencies-list input').change(function() {
        const input = $(this);
        const item = dependencies.find((item) => item.id === input.data('id'));
        if (input.is(':checked')) {
            addToDep(item);
        } else {
            removeFromDep(item);
        }
    });
    */
}

function closeModal() {
    $('.modal .modal-body').scrollTop(0);
    $('#dependencies-list').html('');

    $('body').removeClass('dependencies');
    $('#overlay').fadeOut(100);
    $('#wrapper-modal').fadeOut(150);
}

function openModal() {
    modalHeight();
    populateDependencies(dependencies);

    $('body').addClass('dependencies');
    $('#overlay').fadeIn(100);
    $('#wrapper-modal').fadeIn(150);
}

$(function () {
    $.getJSON('/ui/dependencies')
        .done(function (data) {
            dependencies = data.dependencies;
            search.addDocuments(data.dependencies);
            updateDepsToAdd();
            renderDepsToAdd();
            rendersDep();
        });

    $('#input-boot-version').change(function () {
        updateDepsToAdd();
        renderDepsToAdd();
        rendersDep();
    });

    // -------------------------------

    // More

    const moreBlock = $('#more-block');
    const fewerOptions = $('#fewer-options');
    const moreOptions = $('#more-options');

    $('#fewer-options button').click(function () {
        fewerOptions.addClass('hide');
        moreOptions.removeClass('hide');
        //moreBlock.removeClass('in');
        moreBlock.slideUp(200, function () {
            updateBodyClass();
        });
    });

    $('#more-options button').click(function () {
        moreOptions.addClass('hide');
        fewerOptions.removeClass('hide');
        moreBlock.slideDown(200, function () {
            updateBodyClass();
        });
    });

    $(window).resize(function () {
        updateBodyClass();
        modalHeight();
    });

    const version = new Versions();
    const bootVersion$ = $('#input-boot-version');

    $('#inputSearch').keyup(function () {
        updateDepsToAdd();
        renderDepsToAdd();
    }).keydown(function (e) {
        let items;
        switch (e.keyCode) {
            case 40:
                items = $('#list-to-add .item');
                if (selected < items.length - 1) {
                    selected++;
                }
                break;
            case 38:
                if (selected > 0) {
                    selected--;
                }
                e.preventDefault();
                break;
            case 13:
                items = $('#list-to-add .item');
                if (selected > -1 && selected < items.length) {
                    const item = listToAdd[selected];
                    if (item.versionRange && !version.matchRange(item.versionRange)(bootVersion$.val())) {
                        // Invalid
                    } else {
                        addToDep(item);
                        if (listToAdd.length === 1) {
                            selected = 0;
                        }
                    }
                }
                e.preventDefault();
                break;
            case 39:
            case 37:
                break;
            case 27:
                $('#inputSearch').val('');
                selected = 0;
                break;
            default:
                selected = 0;
        }
        applySelected();
    }).focus(function () {
        selected = 0;
        applySelected();
    }).blur(function () {
        selected = -1;
        applySelected();
    });

    $('#see-all').click(function () {
        openModal();
    });

    $('.modal').click(function (e) {
        e.stopImmediatePropagation();
    });

    $('.close-modal').click(function () {
        closeModal();
    });

    $('#btn-validate').click(function () {
        const inputs = $('#modal-dependencies .modal-body input:checked');
        const arr = [];
        inputs.each(function() {
            const input = $(this);
            arr.push(dependencies.find((item) => item.id === input.data('id')));
        });
        listAdded = arr;
        updateDepsToAdd();
        rendersDep();
        renderDepsToAdd();
        closeModal();
    });

    /*
    $('.wrapper-modal, .close-modal').click(function () {
        closeModal();
    });
    */

    Mousetrap.bind(['escape'], function () {
        closeModal();
    });

    Mousetrap.bind(['command+enter', 'alt+enter'], function (e) {
        $('.submit .btn-primary').click();
    });

});