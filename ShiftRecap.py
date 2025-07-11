"""
Author: Jarrin Smith
Date: March 16, 2025
Version: 1.0
"""

import tkinter as tk
from tkinter import messagebox
from tkinter import filedialog

def save_recap():
    recap = ""
    user_name = name_entry.get().strip()  # Get the user's name
    if user_name:
        recap += f"Recap by: {user_name}\n\n"

    for i, issue_frame in enumerate(issues_frames):
        shop = issue_frame['shop'].get().strip()
        process_equipment = issue_frame['process_equipment'].get().strip()
        time_of_event = issue_frame['time_of_event'].get().strip()
        time_of_resolution = issue_frame['time_of_resolution'].get().strip()
        error_message = issue_frame['error_message'].get().strip()
        event_description = issue_frame['event_description'].get().strip()
        actions_taken = issue_frame['actions_taken'].get().strip()
        corrective_actions = issue_frame['corrective_actions'].get().strip()
        root_cause = issue_frame['root_cause'].get().strip()
        issue_resolution_file_name = issue_frame['issue_resolution_file_name'].get().strip()
        recap += f"""  • Shop: {shop}
      Process/Equipment #: {process_equipment}

  • Time of Event: {time_of_event}
      Time of Resolution: {time_of_resolution}

  • Error Message: {error_message}
      Event Description: {event_description}

  • Actions Taken: {actions_taken}
      Corrective Actions: {corrective_actions}

  • Root Cause: {root_cause}\n
  • Issue Resolution File Name: {issue_resolution_file_name}

"""
        # Add separator only if it's not the last issue
        if i < len(issues_frames) - 1:
            recap += "-"*100 + "\n\n"

    if recap:
        file_path = filedialog.asksaveasfilename(defaultextension=".txt", 
                                                 filetypes=[("Text files", "*.txt"), ("All files", "*.*")])
        if file_path:
            try:
                with open(file_path, 'w', encoding='utf-8') as file:
                    file.write(recap)
                messagebox.showinfo("Success", "Recap saved successfully!")
                root.destroy() 
            except Exception as e:
                messagebox.showerror("Error", f"Failed to save file: {e}")
    else:
        messagebox.showwarning("Warning", "Recap cannot be empty.")

def add_issue():
    issue_frame = {}
    frame = tk.Frame(issues_container, padx=10, pady=10, relief=tk.RIDGE, bd=2)
    frame.pack(pady=10, fill=tk.X)

    tk.Label(frame, text="Shop:", font=font).grid(row=0, column=0, sticky=tk.W, pady=2)
    issue_frame['shop'] = tk.Entry(frame, width=50, font=font)
    issue_frame['shop'].grid(row=0, column=1, pady=2)

    tk.Label(frame, text="Process/Equipment #:", font=font).grid(row=1, column=0, sticky=tk.W, pady=2)
    issue_frame['process_equipment'] = tk.Entry(frame, width=50, font=font)
    issue_frame['process_equipment'].grid(row=1, column=1, pady=2)

    tk.Label(frame, text="Time of Event:", font=font).grid(row=2, column=0, sticky=tk.W, pady=2)
    issue_frame['time_of_event'] = tk.Entry(frame, width=50, font=font)
    issue_frame['time_of_event'].grid(row=2, column=1, pady=2)

    tk.Label(frame, text="Time of Resolution:", font=font).grid(row=3, column=0, sticky=tk.W, pady=2)
    issue_frame['time_of_resolution'] = tk.Entry(frame, width=50, font=font)
    issue_frame['time_of_resolution'].grid(row=3, column=1, pady=2)

    tk.Label(frame, text="Error Message:", font=font).grid(row=4, column=0, sticky=tk.W, pady=2)
    issue_frame['error_message'] = tk.Entry(frame, width=50, font=font)
    issue_frame['error_message'].grid(row=4, column=1, pady=2)

    tk.Label(frame, text="Event Description:", font=font).grid(row=5, column=0, sticky=tk.W, pady=2)
    issue_frame['event_description'] = tk.Entry(frame, width=50, font=font)
    issue_frame['event_description'].grid(row=5, column=1, pady=2)

    tk.Label(frame, text="Actions Taken:", font=font).grid(row=6, column=0, sticky=tk.W, pady=2)
    issue_frame['actions_taken'] = tk.Entry(frame, width=50, font=font)
    issue_frame['actions_taken'].grid(row=6, column=1, pady=2)

    tk.Label(frame, text="Corrective Actions:", font=font).grid(row=7, column=0, sticky=tk.W, pady=2)
    issue_frame['corrective_actions'] = tk.Entry(frame, width=50, font=font)
    issue_frame['corrective_actions'].grid(row=7, column=1, pady=2)

    tk.Label(frame, text="Root Cause:", font=font).grid(row=8, column=0, sticky=tk.W, pady=2)
    issue_frame['root_cause'] = tk.Entry(frame, width=50, font=font)
    issue_frame['root_cause'].grid(row=8, column=1, pady=2)

    tk.Label(frame, text="Issue Resolution File Name:", font=font).grid(row=9, column=0, sticky=tk.W, pady=2)
    issue_frame['issue_resolution_file_name'] = tk.Entry(frame, width=50, font=font)
    issue_frame['issue_resolution_file_name'].grid(row=9, column=1, pady=2)

    issues_frames.append(issue_frame)

def show_help():
    help_window = tk.Toplevel(root)
    help_window.title("Help Guide")
    help_text = tk.Text(help_window, wrap="word", width=80, height=20, font=font)
    help_text.pack(padx=10, pady=10)
    help_content = """
    Help Guide: Shift Recap Tool
    By Jarrin Smith

    Overview:
    The Shift Recap Tool is a Python-based GUI application that allows you to record and save shift recap notes. You can add multiple shop issues. The tool also provides the option to save the recap to a .txt file.

    Features:
    - Add multiple shop issues with detailed information.
    - Save the recap to a .txt file.

    How to Use:
    1. Launch the Application:
       - Double-click the executable file (run_shift_recap.exe) to launch the application. This will open a GUI window.

    2. Add Shop Issues:
       - Click the "Add Issue" button to add a new set of input fields for a shop issue.
       - Fill in the details for each issue:
         - Shop: Enter the shop name.
         - Process/Equipment #: Enter the process or equipment number.
         - Time of Event: Enter the time the event was reported by operations.
         - Time of Resolution: Enter the time operations were notified of the resolution.
         - Error Message: Enter the error message from the TC/MOS log or MES screen number if applicable.
         - Event Description: Enter the event description (can be copy/paste of chat message from operations).
         - Actions Taken: Enter the troubleshooting steps taken.
         - Corrective Actions: Enter the actions taken to resolve the issue.
         - Root Cause: Enter the root cause of the issue.
         - Issue Resolution File Name: Enter the file name of the issue resolution document.

    3. Save the Recap:
       - Click the "Save" button to save the recap.
       - A file dialog will appear. Choose the location and name for the .txt file and click "Save".
       - If the recap is saved successfully, a success message will appear, and the application will close.

    Example Usage:
    1. Multiple Shop Issues:
       - Click "Add Issue" to add the first issue.
       - Fill in the details for the first issue.
       - Click "Add Issue" again to add another issue.
       - Fill in the details for the second issue.
       - Repeat as needed for additional issues.
       - Click "Save".
       - Choose the location and name for the .txt file and click "Save".
       - The recap will be saved with the details of all the issues.

    """
    
    help_text.insert("1.0", help_content)
    help_text.config(state="disabled")

def on_mouse_wheel(event):
    canvas.yview_scroll(int(-1 * (event.delta / 120)), "units")

root = tk.Tk()
root.title("Shift Recap")
root.geometry("1000x800") 

font = ("Helvetica", 10)

title_label = tk.Label(root, text="Shift Recap Tool", font=("Helvetica", 16, "bold"))
title_label.pack(pady=10)

name_label = tk.Label(root, text="Your Name:", font=font)
name_label.pack(pady=5)
name_entry = tk.Entry(root, width=50, font=font)
name_entry.pack(pady=5)

# Frame for text fields with scroll
canvas = tk.Canvas(root)
scrollbar = tk.Scrollbar(root, orient="vertical", command=canvas.yview)
issues_container = tk.Frame(canvas)

canvas.configure(yscrollcommand=scrollbar.set)

scrollbar.pack(side="right", fill="y")
canvas.pack(side="left", fill="both", expand=True)
canvas.create_window((0, 0), window=issues_container, anchor="nw")

# Update scroll region on resize
def on_frame_configure(event):
    canvas.configure(scrollregion=canvas.bbox("all"))

issues_container.bind("<Configure>", on_frame_configure)

# Initialize the issues_frames list
issues_frames = []

# Add first issue
add_issue()

add_issue_button = tk.Button(root, text="Add Issue", command=add_issue, font=font)
add_issue_button.pack(pady=10)

save_button = tk.Button(root, text="Save", command=save_recap, font=font)
save_button.pack(pady=10)

help_button = tk.Button(root, text="Help", command=show_help, font=font)
help_button.pack(pady=10)

root.bind_all("<MouseWheel>", on_mouse_wheel)  # Bind mouse wheel for scrolling

root.mainloop()