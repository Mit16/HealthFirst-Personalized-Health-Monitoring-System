import React, { useState, useContext, useEffect } from "react";
import { toast } from "react-toastify";
import { HealthContext } from "../context/HealthContext";
import { Label } from "../components/ui/label";
import { Input } from "../components/ui/input";
import { cn } from "../lib/utils";
import Header from "../components/Header";
import { FooterComponent } from "../components/FooterComponent";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format, differenceInMinutes } from "date-fns";

const HealthForm = () => {
  const {
    submitHealthMetrics,
    submitDailyMetrics,
    fetchAllHealthData,
    loading,
    setLoading,
  } = useContext(HealthContext);

  const [form, setForm] = useState({
    heartRate: "",
    bodyTemperature: "",
    spo2: "",
    accX: "",
    accY: "",
    accZ: "",
    steps: "",
    sleepStartTime: new Date(),
    sleepEndTime: new Date(),
    sleepDuration: "",
  });


  const validateInputs = () => {
    const hr = Number(form.heartRate);
    const temp = Number(form.bodyTemperature);
    const spo2 = Number(form.spo2);

    if (hr < 40 || hr > 200) {
      toast.error("Heart rate must be between 40 and 200 bpm");
      return false;
    }
    if (temp < 30 || temp > 45) {
      toast.error("Body temperature must be between 30°C and 45°C");
      return false;
    }
    if (spo2 < 50 || spo2 > 100) {
      toast.error("SpO2 must be between 50% and 100%");
      return false;
    }
    return true;
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleDateChange = (name, date) => {
    const newForm = {
      ...form,
      [name]: date,
    };

    if (newForm.sleepStartTime && newForm.sleepEndTime) {
      const minutes = differenceInMinutes(
        newForm.sleepEndTime,
        newForm.sleepStartTime
      );

      if (minutes <= 0) {
        toast.warn("⚠️ Sleep end time must be after start time.");
        newForm.sleepDuration = "";
      } else {
        if (minutes < 30) {
          toast.warn("⚠️ Sleep duration seems too short.");
        } else if (minutes > 960) {
          toast.warn("⚠️ Sleep duration seems too long.");
        }

        const hrs = Math.floor(minutes / 60);
        const mins = minutes % 60;
        newForm.sleepDuration = `${hrs} hrs ${mins} min`;
      }
    }

    setForm(newForm);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateInputs()) return;

    setLoading(true);
    try {
      const payload = {
        ...form,
        sleepStartTime: form.sleepStartTime
          ? form.sleepStartTime.getTime()
          : null,
        sleepEndTime: form.sleepEndTime ? form.sleepEndTime.getTime() : null,
        sleepDuration:
          form.sleepStartTime && form.sleepEndTime
            ? form.sleepEndTime.getTime() - form.sleepStartTime.getTime()
            : null,
        date: format(new Date(), "yyyy-MM-dd"),
      };

      await submitHealthMetrics(payload);
      await submitDailyMetrics(payload);
      toast.success(
        "✅ Data submitted successfully! Visit Prediction to see result."
      );

      setForm({
        heartRate: "",
        bodyTemperature: "",
        spo2: "",
        accX: "",
        accY: "",
        accZ: "",
        steps: "",
        sleepStartTime: new Date(),
        sleepEndTime: new Date(),
        sleepDuration: "",
      });

      await fetchAllHealthData();
    } catch (err) {
      console.error(err);
      toast.error("❌ Submission failed.");
    } finally {
      setLoading(false);
    }
  };

  // Custom input to make DatePicker look like other Inputs
  const CustomInput = React.forwardRef(({ value, onClick }, ref) => (
    <Input
      onClick={onClick}
      ref={ref}
      value={value}
      readOnly
      className="cursor-pointer"
      required
    />
  ));

  return (
    <>
      <Header />
      <div className="shadow-input mx-auto w-full max-w-md rounded-none bg-white p-4 md:rounded-2xl md:p-8 dark:bg-black">
        <h2 className="text-xl font-bold text-neutral-800 dark:text-neutral-200">
          Submit Health Data
        </h2>
        <p className="mt-2 text-sm text-neutral-600 dark:text-neutral-300">
          Fill in your recent health metrics
        </p>

        <form className="my-8" onSubmit={handleSubmit}>
          {[
            "heartRate",
            "bodyTemperature",
            "spo2",
            "accX",
            "accY",
            "accZ",
            "steps",
          ].map((field) => (
            <LabelInputContainer key={field} className="mb-4">
              <Label htmlFor={field}>{field.replace(/([A-Z])/g, " $1")}</Label>
              <Input
                id={field}
                name={field}
                placeholder={field.replace(/([A-Z])/g, " $1")}
                value={form[field]}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </LabelInputContainer>
          ))}

          <LabelInputContainer className="mb-4">
            <Label>Sleep Start Time</Label>
            <DatePicker
              selected={form.sleepStartTime}
              onChange={(date) => handleDateChange("sleepStartTime", date)}
              showTimeSelect
              dateFormat="Pp"
              customInput={<CustomInput />}
            />
          </LabelInputContainer>

          <LabelInputContainer className="mb-4">
            <Label>Sleep End Time</Label>
            <DatePicker
              selected={form.sleepEndTime}
              onChange={(date) => handleDateChange("sleepEndTime", date)}
              showTimeSelect
              dateFormat="Pp"
              customInput={<CustomInput />}
            />
          </LabelInputContainer>

          <LabelInputContainer className="mb-4">
            <Label htmlFor="sleepDuration">Sleep Duration</Label>

            <Input
              id="sleepDuration"
              name="sleepDuration"
              value={form.sleepDuration}
              disabled
              readOnly
              required
            />
          </LabelInputContainer>

          <button
            type="submit"
            className="group/btn relative block h-10 w-full rounded-md bg-gradient-to-br from-black to-neutral-600 font-medium text-white shadow-[inset_0px_1px_0px_#ffffff40,inset_0px_-1px_0px_#ffffff40] dark:from-zinc-900 dark:to-zinc-900 dark:shadow-[inset_0px_1px_0px_#27272a,inset_0px_-1px_0px_#27272a]"
            disabled={loading || !form.sleepDuration} // ✅ updated here
          >
            {loading ? "Submitting..." : "Submit"}
            <BottomGradient />
          </button>
        </form>
      </div>
      <FooterComponent />
    </>
  );
};

const BottomGradient = () => (
  <>
    <span className="absolute inset-x-0 -bottom-px block h-px bg-gradient-to-r from-transparent via-cyan-500 to-transparent opacity-0 transition duration-500 group-hover/btn:opacity-100" />
    <span className="absolute inset-x-10 -bottom-px mx-auto block h-px w-1/2 bg-gradient-to-r from-transparent via-indigo-500 to-transparent opacity-0 blur-sm transition duration-500 group-hover/btn:opacity-100" />
  </>
);

const LabelInputContainer = ({ children, className }) => (
  <div className={cn("flex w-full flex-col space-y-2", className)}>
    {children}
  </div>
);

export default HealthForm;
