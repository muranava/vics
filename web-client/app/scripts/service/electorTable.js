/**
 * Service to provide canvass card PDF generation.
 * Uses jsPDF to perform the rendering
 * see https://github.com/MrRio/jsPDF
 */
angular
    .module('canvass')
    .service('electorTable', function () {
        var api = {},
            img = new Image();
        img.src = 'images/logo.jpg';

        var theme = 'grid',
            columnStyles = {
                house: {columnWidth: 80},
                name: {columnWidth: 145},
                telephone: {columnWidth: 90},
                likelihood: {columnWidth: 60, fillColor: [225, 225, 225]},
                iss1: {columnWidth: 50},
                iss2: {columnWidth: 50},
                iss3: {columnWidth: 50},
                support: {columnWidth: 60, fillColor: [225, 225, 225]},
                hasPv: {columnWidth: 27},
                wantsPv: {columnWidth: 27},
                lift: {columnWidth: 27},
                poster: {columnWidth: 27},
                deceased: {columnWidth: 27},
                rollNum: {columnWidth: 80}
            },
            headerStyles = {
                fillColor: [250, 250, 250],
                textColor: 0,
                fontSize: 11,
                rowHeight: 20
            },
            docProperties = {
                subject: 'Canvass Card',
                author: 'Vote Leave',
                creator: 'Canvass Application'
            },
            margin = {top: 100, left: 20},
            columnLabels = {
                house: 'House #',
                name: 'Name',
                tel: 'Tel No.',
                likelihood: 'Voting\nLikelihood',
                iss1: 'Issue1',
                iss2: 'Issue2',
                iss3: 'Issue3',
                intention: 'Voting\nIntention',
                hasPV: 'Has\nPV',
                wantsPV: 'Wants\n  PV',
                lift: 'Lift',
                poster: 'Poster',
                dead: 'Dead',
                rollNum: 'Roll #'
            },
            columnDataModel = [
                {title: columnLabels.house, dataKey: "house"},
                {title: columnLabels.name, dataKey: "name"},
                {title: columnLabels.tel, dataKey: "telephone"},
                {title: columnLabels.likelihood, dataKey: "likelihood"},
                {title: columnLabels.iss1, dataKey: "iss1"},
                {title: columnLabels.iss2, dataKey: "iss2"},
                {title: columnLabels.iss3, dataKey: "iss3"},
                {title: columnLabels.intention, dataKey: "support"},
                {title: columnLabels.hasPV, dataKey: "hasPv"},
                {title: columnLabels.wantsPV, dataKey: "wantsPv"},
                {title: columnLabels.lift, dataKey: "lift"},
                {title: columnLabels.poster, dataKey: "poster"},
                {title: columnLabels.dead, dataKey: "deceased"},
                {title: columnLabels.rollNum, dataKey: "rollNum"}
            ];

        /**
         * Transforms the elector domain model to the presentation model
         * @param elector
         * @returns {Object}
         */
        function electorToTableData(elector) {
            return {
                house: {house: elector.house, street: elector.street},
                name: elector.lastName + ', ' + elector.firstName,
                telephone: randPhone(), // FIXME get from PAF
                likelihood: '', // empty
                iss1: '',       // empty
                iss2: '',       // empty
                iss3: '',       // empty
                support: '',    // empty
                hasPv: '',      // empty
                wantsPv: '',    // empty
                lift: '',       // empty
                poster: '',     // empty
                deceased: '',   // empty
                rollNum: buildElectorId(elector)
            };
        }

        /**
         * Renders a pdf from a list of electors
         * @param {Object} config
         * @param {Object} doc
         */
        api.renderPdfTable = function (config, doc) {
            var currHouse,
                prevHouse,
                deadPos,
                posterPos,
                liftPos,
                wantsPVPos,
                hasPVPos,
                slantTextConf = {x: 11, y: 40, angle: 75};

            var data = {
                electors: config.electors,
                wardName: config.wardName,
                wardCode: config.wardCode,
                streetName: config.street,
                constituencyName: config.constituencyName
            };

            var rows = _.map(data.electors, function (elector) {
                return electorToTableData(elector);
            });
            var sorted = _.sortBy(rows, function (r) {
                return [r.street, r.house];
            });

            function drawLegend() {
                var yPosIntentionLegend = 720,
                    yPosLikelihoodLegend = 600,
                    topMargin = 30,
                    ySpacing = 10;

                doc.setFontSize(10);
                doc.text("Voting intention:", yPosIntentionLegend, topMargin);
                doc.text("1 Def. Remain", yPosIntentionLegend, topMargin + ySpacing);
                doc.text("2 Prob. Remain", yPosIntentionLegend, topMargin + ySpacing * 2);
                doc.text("3 Undecided", yPosIntentionLegend, topMargin + ySpacing * 3);
                doc.text("4 Prob. Leave", yPosIntentionLegend, topMargin + ySpacing * 4);
                doc.text("5 Def. Leave", yPosIntentionLegend, topMargin + ySpacing * 5);

                doc.text("Voting likelihood:", yPosLikelihoodLegend, topMargin);
                doc.text("1 Def. won't vote", yPosLikelihoodLegend, topMargin + ySpacing);
                doc.text("2 Prob. won't vote", yPosLikelihoodLegend, topMargin + ySpacing * 2);
                doc.text("3 Undecided", yPosLikelihoodLegend, topMargin + ySpacing * 3);
                doc.text("4 Prob. will vote", yPosLikelihoodLegend, topMargin + ySpacing * 4);
                doc.text("5 Def. will vote", yPosLikelihoodLegend, topMargin + ySpacing * 5);
            }

            function drawLogoHeader() {
                doc.addImage(img, 'JPEG', 15, 15, 60, 60);
                doc.setFontSize(20);
                doc.text("Vote Leave", 80, 30);
                doc.setFontSize(12);
                doc.text("Constituency: " + data.constituencyName.split("-")[1], 80, 50);
                doc.text("Ward: " + data.wardName, 80, 65);
                doc.text("Street: " + data.streetName, 80, 80);
            }

            var options = {
                theme: theme,
                columnStyles: columnStyles,
                headerStyles: headerStyles,
                styles: {
                    rowHeight: 23
                },
                margin: margin,
                beforePageContent: function () {
                    drawLogoHeader(doc, data);
                    drawLegend(doc);
                },
                /**
                 * Unfortunately jsPDF autotable does not support header cells with slanted text,
                 * so here we remove the current cell text, calculate the text position in the document and later
                 * re draw the slanted text in the afterPageContent hook. Urgh...
                 */
                drawHeaderCell: function (cell) {
                    if (cell.raw === columnLabels.dead) {
                        deadPos = {x: cell.x, y: cell.y};
                        clearCellText(doc, cell);
                    } else if (cell.raw === columnLabels.poster) {
                        posterPos = {x: cell.x, y: cell.y};
                        clearCellText(doc, cell);
                    } else if (cell.raw === columnLabels.lift) {
                        liftPos = {x: cell.x, y: cell.y};
                        clearCellText(doc, cell);
                    } else if (cell.raw === columnLabels.wantsPV) {
                        wantsPVPos = {x: cell.x, y: cell.y};
                        clearCellText(doc, cell);
                    } else if (cell.raw === columnLabels.hasPV) {
                        hasPVPos = {x: cell.x, y: cell.y};
                        clearCellText(doc, cell);
                    }
                },
                afterPageContent: function (data) {
                    var str = "Page " + data.pageCount;
                    doc.text(str, data.table.width - data.settings.margin.right / 2, doc.internal.pageSize.height - 20);

                    doc.setFontSize(10);
                    doc.text(columnLabels.dead, deadPos.x + slantTextConf.x, deadPos.y + slantTextConf.y, null, slantTextConf.angle);
                    doc.text(columnLabels.poster, posterPos.x + slantTextConf.x, posterPos.y + slantTextConf.y, null, slantTextConf.angle);
                    doc.text(columnLabels.lift, liftPos.x + slantTextConf.x + 4, liftPos.y + slantTextConf.y - 4, null, slantTextConf.angle);
                    doc.text(columnLabels.wantsPV, wantsPVPos.x + slantTextConf.x - 1, wantsPVPos.y + slantTextConf.y, null, slantTextConf.angle);
                    doc.text(columnLabels.hasPV, hasPVPos.x + slantTextConf.x - 1, hasPVPos.y + slantTextConf.y - 4, null, slantTextConf.angle);
                },
                drawRow: function (row, data) {
                    currHouse = row.cells.house && row.cells.house.raw.house;

                    if (currHouse && (prevHouse !== currHouse)) {
                        doc.rect(data.settings.margin.left, row.y, data.table.width + 18, 20, 'F');
                        data.cursor.y += 2;
                    }
                    prevHouse = currHouse;
                },
                createdCell: function(cell, data) {
                    if (data.column.dataKey === 'house') {
                        cell.text = data.row.cells.house.raw.house;
                    }
                }
            };

            docProperties.title = data.constituencyName;
            doc.setProperties(docProperties);
            doc.autoTable(columnDataModel, sorted, options);
            doc.setFontSize(22);
            return doc;
        };

        function clearCellText(doc, cell) {
            doc.setTextColor(250, 250, 250);
            cell.fontSize = 0;
        }

        /**
         * The elector ID is a compound of the following:
         *
         *  {ward code}/{elector id}/{elector suffix}
         *
         * @param {Object} elector
         * @returns {String} the formatted elector Id
         */
        function buildElectorId(elector) {
            var wardCode = elector.wardCode,
                electorId = elector.electorId,
                electorSuffix = elector.electorSuffix;
            var mandatoryFields = wardCode + '/' + electorId;
            return electorSuffix ? mandatoryFields + '/' + electorSuffix : mandatoryFields;
        }

        /**
         * FIXME remove this when data available in PAF
         */
        function randPhone() {
            return '0' + _.random(7000000000, 8999999999);
        }

        return api;
    });
