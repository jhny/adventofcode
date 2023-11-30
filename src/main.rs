use clap::{arg, Command};
use std::fs;
use std::path::Path;

fn main() {
    let matches = Command::new("Advent of Code File Creator")
        .version("1.0")
        .author("Your Name")
        .about("Creates a Clojure file for a specific Advent of Code day")
        .arg(arg!(-d --day <DAY> "Sets the day for the challenge").required(true))
        .arg(arg!(-y --year <YEAR> "Sets the year for the challenge").default_value("2023"))
        .get_matches();

    let day = matches.get_one::<String>("day").unwrap();
    let year = matches.get_one::<String>("year").unwrap();
    create_file(day, year);
    create_input_file(day, year);
    create_sample_file(day, year);

    create_rust_file(day, year);
    replace_rust_main_file(day, year);
}

fn create_file_with_content(filename: &str, file_content: &str) {
    if !Path::new(filename).exists() {
        match fs::write(filename, file_content) {
            Ok(_) => {
                println!("Created file: {}", filename);
            }
            Err(_) => {
                println!("Failed to create file: {}", filename);
            }
        }
    } else {
        println!("File already exists: {}", filename);
    }
}

fn create_file(day: &str, year: &str) {
    let filename = format!("./{}/src/day{}.clj", year, day);
    let file_content = format!(
        r#"(ns day{}
  (:require [clojure.string :as str]))

(def sample (slurp "resources/day{}/sample_input.txt"))

(def input (slurp "resources/day{}/input.txt"))
"#,
        day, day, day
    );

    create_file_with_content(&filename, &file_content);
}

fn create_input_file(day: &str, year: &str) {
    let filename = format!("./{}/resources/day{}/input.txt", year, day);
    let file_content = format!("");

    fs::create_dir_all(format!("./{}/resources/day{}", year, day))
        .expect("Failed to create directory");
    create_file_with_content(&filename, &file_content);
}

fn create_sample_file(day: &str, year: &str) {
    let filename = format!("./{}/resources/day{}/sample_input.txt", year, day);
    let file_content = format!("");

    fs::create_dir_all(format!("./{}/resources/day{}", year, day))
        .expect("Failed to create directory");
    create_file_with_content(&filename, &file_content);
}

fn create_rust_file(day: &str, year: &str) {
    let filename = format!("./{}/src/day{}.rs", year, day);
    let file_content = format!(
        r#"
fn run1() {{

}}
fn run2() {{

}}"#
    );

    create_file_with_content(&filename, &file_content);
}

fn replace_rust_main_file(day: &str, year: &str) {
    let filename = format!("./{}/src/main.rs", year);
    let file_content = format!(
        r#"// Path: main 
mod day{};

fn main() {{
    day{}::run1(); 
    day{}::run2(); 
}}"#,
        day, day, day
    );

    create_file_with_content(&filename, &file_content);
}
