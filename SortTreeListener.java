import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import java.lang.reflect.Array;
import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;



public class SortTreeListener implements SelectionListener {

	@Override
	public void widgetSelected(SelectionEvent e) {
		sortTree(e);

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}
	private void sortTree(SelectionEvent e) {
		TreeColumn column = (TreeColumn) e.widget; // name of the column which was selected for sorting
		System.out.println(column);
		Tree tree = column.getParent(); //Tree
		TreeItem[] treeItems = tree.getItems(); //TreeItem0 , ...
		TreeColumn sortColumn = tree.getSortColumn();
		System.out.println(sortColumn);
		TreeColumn columns[] = tree.getColumns(); // TreeColumn{TreeColumn}, ...
		tree.setSortColumn(column);
		int numOfColumns = columns.length; //numOfColumns = 2
		int columnIndex = this.findColumnIndex(columns, column, numOfColumns); //position of column = 1
		
		Collator collator = Collator.getInstance(Locale.getDefault());
		Boolean sort = false;
		Pattern pattern = Pattern.compile("([\\+]*|[\\-]*)\\d+");
		
		if ((column.equals(sortColumn)) && (tree.getSortDirection() == SWT.UP)) {
		} else {
			tree.setSortDirection(SWT.UP); //Ascending order
			for (int i = 1; i < treeItems.length; i++) {
				String value1 = treeItems[i].getText(columnIndex).trim(); //one with the ">" i indicates row value. value present in the column of the corresponding row
				for (int j = 0; j < i; j++) {
					String value2 = treeItems[j].getText(columnIndex).trim();//value in the next row/previous row
					//value1 and value 2 indicate root nodes being compared
					if (pattern.matcher(value1).matches() && pattern.matcher(value2).matches()) {
						double d1 = this.getDouble(value1);
						double d2 = this.getDouble(value2);
						if (d1 < d2) {
							sort = true;
						}
					} 
					if (sort) {
						String[] values = this.getColumnValues(treeItems[i],numOfColumns);//value present in the corresponding column of the row j [TreeItem3, -1380695479]
						System.out.println(treeItems[i].getItemCount());
						TreeItem[] subItems = treeItems[i].getItems();
//						for(int ii=0; ii< subItem.length ; ii++){
						System.out.println(Arrays.toString(subItems));
//						}
//						TreeItem[] subItems = treeItems[i].getItems(); //getItems ->[TreeItem {TreeItem0}, TreeItem {TreeItem1}, TreeItem {TreeItem2}, TreeItem {TreeItem3}, TreeItem {TreeItem4}]
						//value inside the tree items of i -> subItems TreeItem{SubSubTreeItem0}, ...
						
						TreeItem item = new TreeItem(tree, SWT.NONE, j);//swapping to jth position
						 item.setText(values);//item = TreeItem {TreeItem3} and values = [TreeItem3, -1380695479] // writing into jth position
						for (TreeItem si : subItems) {//writing values inside TreeItem3
							TreeItem subItem = new TreeItem(item, SWT.NONE);
							subItem.setText(this.getColumnValues(si, numOfColumns));//numofColumns = 2, si = TreeItem {SubSubTreeItem0},TreeItem {SubSubTreeItem1}
						}
						treeItems[i].dispose();//ith tree gets disposed
						//[TreeItem {TreeItem0}, TreeItem {TreeItem1}, TreeItem {TreeItem2}, TreeItem {*Disposed*}, TreeItem {TreeItem4}]
						treeItems = tree.getItems();//[TreeItem {TreeItem3}, TreeItem {TreeItem0}, TreeItem {TreeItem1}, TreeItem {TreeItem2}, TreeItem {TreeItem4}]
						sort = false;
						break;
					}
				}
			}
		}
	}
	/**
	 * Find the index of a column
	 * 
	 * @param columns
	 * @param numOfColumns
	 * @return int
	 */
	private int findColumnIndex(TreeColumn[] columns, TreeColumn column,
			int numOfColumns) {
		int index = 0;
		for (int i = 0; i < numOfColumns; i++) {
			if (column.equals(columns[i])) {
				index = i;
				break;
			}
		}
		return index;
	}
	/**
	 * Get the double value from a string
	 * 
	 * @param str
	 * @return double
	 */
	private double getDouble(String str) {
		double d;
		if (str.startsWith("+")) {
			d = Double.parseDouble(str.split("\\+")[1]);
		} else {
			d = Double.parseDouble(str);
		}
		return d;
	}

	/**
	 * Get the array of string value from the provided TreeItem
	 * 
	 * @param treeItem
	 * @param numOfColumns
	 * @return String[]
	 */
	private String[] getColumnValues(TreeItem treeItem, int numOfColumns) {
		String[] values = new String[numOfColumns];
		for (int i = 0; i < numOfColumns; i++) {
			values[i] = treeItem.getText(i);
		}
		return values;
	}
}
