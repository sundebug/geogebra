package org.geogebra.common.spreadsheet.kernel;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.geogebra.common.kernel.commands.AlgebraProcessor;
import org.geogebra.common.kernel.geos.GeoElementSpreadsheet;
import org.geogebra.common.main.error.ErrorHandler;
import org.geogebra.common.spreadsheet.core.SpreadsheetCellProcessor;
import org.geogebra.common.util.debug.Log;

import com.himamis.retex.editor.share.util.Unicode;

/**
 * Sends spreadsheet cell editor input towards the AlgebraProcessor.
 *
 * (This class is an adapter between the Spreadsheet core and the Kernel.)
 */
public class DefaultSpreadsheetCellProcessor implements SpreadsheetCellProcessor {

	private final AlgebraProcessor algebraProcessor;
	private final ErrorHandler errorHandler;

	/**
	 * Constructor.
	 * @param algebraProcessor {@link AlgebraProcessor}
	 * @param errorHandler The error handler of the cell.
	 */
	public DefaultSpreadsheetCellProcessor(@Nonnull AlgebraProcessor algebraProcessor,
			@CheckForNull ErrorHandler errorHandler) {
		this.algebraProcessor = algebraProcessor;
		this.errorHandler = errorHandler;
	}

	/**
	 * Depending on input, processor makes text or evaluates input.
	 *
	 * @param input The input to process.
	 * @param row Identifies the cell to receive the input.
	 * @param column Identifies the cell to receive the input.
	 */
	@Override
	public void process(String input, int row, int column) {
		String cellName = GeoElementSpreadsheet.getSpreadsheetCellName(column, row);
		process(input, cellName);
	}

	/**
	 * Same as {@link #process(String, int, int)}, only with a cell name formed from a row/column
	 * pair.
	 * @param input The input to process.
	 * @param cellName Identifies the cell to receive the input.
	 */
	public void process(String input, String cellName) {
		try {
			processInput(isCommand(input)
					? buildCommandFrom(input, cellName) : buildTextFrom(input, cellName));
		} catch (Exception e) {
			Log.debug("error " + e.getLocalizedMessage());
		}
	}

	private void processInput(String command) {
		algebraProcessor.processAlgebraCommandNoExceptionHandling(command, true,
				errorHandler, false, null);
	}

	private static boolean isCommand(String input) {
		return input.startsWith("=");
	}

	private String buildTextFrom(String input, String cellName) {
		StringBuilder sb = new StringBuilder();
		sb.append(cellName);
		sb.append(Unicode.ASSIGN_STRING);
		sb.append("\"");
		sb.append(input.replaceAll("\"", ""));
		sb.append("\"");
		return sb.toString();
	}

	private String buildCommandFrom(String input, String cellName) {
		StringBuilder sb = new StringBuilder();
		sb.append(cellName);
		sb.append(Unicode.ASSIGN_STRING);
		sb.append(input.substring(1));
		return sb.toString();
	}
}