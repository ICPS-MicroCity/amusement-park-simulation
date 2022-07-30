import pandas as pd
import sys
import glob
import numpy as np


def extract_dataframe(file_name):
    return pd.read_csv(file_name, skiprows=8, header=None, sep="\s+", engine="python")


def extract(df, f, size):
    return [f(df.loc[i][1:]) for i in range(size)]


def get_files(path, data_type, strategy):
    return glob.glob(path + data_type + "*\"" + strategy + "\"*.csv")


def main(path, data_type, strategy):
    ops = [np.mean, np.sum, np.var]
    n_lines = 22

    match data_type:
        case "ratio":
            s_dfs = [extract_dataframe(file_name) for file_name in get_files(path, "satisfactions", strategy)]
            wt_dfs = [extract_dataframe(file_name) for file_name in get_files(path, "waitingTime", strategy)]
            s_mean = np.mean([extract(df, ops[0], n_lines) for df in s_dfs], axis=0)
            wt_mean = np.mean([extract(df, ops[0], n_lines) for df in wt_dfs], axis=0)
            print("ratio:", np.divide(s_mean, wt_mean))
        case _:
            dfs = [extract_dataframe(file_name) for file_name in get_files(path, data_type, strategy)]
            results = [[extract(df, op, n_lines) for df in dfs] for op in ops]
            print("mean " + data_type + ":", np.mean(results[0], axis=0))


if __name__ == "__main__":
    match len(sys.argv):
        case 4:
            main(sys.argv[1], sys.argv[2], sys.argv[3])
        case _:
            print("In order to run the program you must specify the arguments:\n\n",
                  "\tpython3 data-evaluation.py <path-to-files> <data_type> <strategy>\n\n",
                  "<data-type> can be one among \"satisfactions\", \"waitingTime\" and \"ratio\"\n",
                  "<strategy> can be one among \"random\", \"shortestQueue\" and \"shortestQueueRange\"\n")
